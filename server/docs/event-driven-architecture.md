# Event-Driven Architecture

## Phase 4 — `SaleCompleted` (implemented)

One write, three independent reactions. The `sales` module knows nothing about its consumers.

```
POST /api/v1/sales
        │
        ▼
  RecordSaleUseCase          ─── one transaction ───┐
   ├─ price from ProductCatalog                      │
   ├─ save Sale                                      │  sale row +
   └─ ApplicationEventPublisher.publishEvent(...)    │  3 event_publication rows
        │                                            │  committed atomically
        ▼                                            │
   SaleCompleted ────────────────────────────────────┘
        │
        │  after commit, async, one transaction + one publication row per listener
        ├──────────────┬──────────────────────┐
        ▼              ▼                      ▼
   Inventory       Accounting             Reporting
   (tank)          (accounting)           (reporting)
   draw down       DR settlement          insert sales_fact
   tank stock      CR fuel revenue        (star-schema fact row)
```

### Why `@ApplicationModuleListener`

Each consumer uses `@ApplicationModuleListener`, which is `@Async` +
`@TransactionalEventListener(AFTER_COMMIT)` + `@Transactional(REQUIRES_NEW)`, and — because
`spring-modulith-starter-jpa` is on the classpath — writes a row to `event_publication`
**inside the publishing transaction**. That is a transactional outbox:

- The sale and its three pending deliveries commit or roll back together. No event is ever
  published for a sale that did not persist, and no sale persists without its events queued.
- Each listener gets its **own** publication row, so one consumer failing does not affect the
  others. This was verified against a live database: a zero-value sale left the accounting
  publication `PUBLISHED` (incomplete) while inventory and reporting both completed.
- Incomplete publications are resubmitted on restart
  (`spring.modulith.events.republish-outstanding-events-on-restart: true`). The same run showed
  `completion_attempts=2` on the accounting listener after the retry succeeded.

### Consistency model

Inventory, accounting and reporting are **eventually** consistent with the sale. That is the
correct model here: the fuel has physically left the pump before the API is called, so the
downstream modules are catching up with reality rather than authorising it.

Consequences that were handled explicitly:

| Concern | Handling |
| --- | --- |
| Redelivery (at-least-once) | Accounting keys on `journal_entries.reference = SALE-<uuid>`; reporting keys on `sales_facts.sale_reference`. Both unique, both checked before insert. |
| Poison messages | A listener that can never succeed would retry forever. Zero-value sales are *skipped* by accounting rather than throwing. Genuinely bad data (unknown tank) still fails loudly and stays in the registry for inspection. |
| Concurrent sales on one tank | `TankRepository.findByIdForUpdate` takes `PESSIMISTIC_WRITE` so parallel draw-downs serialise. |
| Book stock below zero | `Tank.recordSale` floors at zero and returns the shortfall; the listener logs a warning for a physical dip check instead of rejecting a sale that already happened. |
| Payload size | Modulith serialises the event to `event_publication.serialized_event`. The original column was `varchar(255)`; a real `SaleCompleted` serialises to ~252 characters. Migration `009` widens it to `text`. |

### Module boundaries

`ApplicationModules.verify()` passes. Cross-module contact is limited to:

```
sales ──────► product          (ProductCatalog / ProductSnapshot, module root = published API)
tank ───────► sales            (SaleCompleted only)
accounting ─► sales            (SaleCompleted only)
reporting ──► sales            (SaleCompleted only)
```

`sales` deliberately does **not** depend on `tank`. Adding a synchronous tank validation would
create a `sales ↔ tank` cycle and Modulith would reject it. Tank correctness is enforced by the
inventory listener instead.

Internals stay internal: `Sale`, `Product` and `Tank` never cross a module boundary. Only the
event record and the two `product` API types are exposed.

## Phase 5 — evolving to a broker

The modules are already split along the seams a broker would need. Nothing about the domain code
changes; only the transport does.

```
                        today                              after
                 ┌────────────────┐              ┌────────────────┐
                 │ Spring Boot    │              │ sales service  │
                 │                │              └───────┬────────┘
                 │  sales         │                      │ SaleCompleted
                 │    │           │                      ▼
                 │    ▼           │              ┌────────────────┐
                 │  event_        │              │ Kafka / Rabbit │
                 │  publication   │              └───┬────┬────┬──┘
                 │    │           │                  │    │    │
                 │  ┌─┼─┐         │              ┌───▼┐ ┌─▼──┐ ┌▼────────┐
                 │  ▼ ▼ ▼         │              │Inv │ │Acct│ │Reporting│
                 │ Inv Acct Rep   │              └────┘ └────┘ └─────────┘
                 └────────────────┘
```

### Step 1 — externalise the event (no code changes in the modules)

Add `spring-modulith-events-kafka` (or `-amqp`) and annotate the event:

```java
@Externalized("fuel.sales::SaleCompleted")
public record SaleCompleted(...) { }
```

Modulith then publishes to the broker **from the same outbox table** it already uses. In-process
listeners keep working. This is the only step that must happen before a split, and it is the one
that makes the outbox valuable: the broker send is retried from a committed row, not fired
inside a transaction that might roll back.

### Step 2 — move one consumer out

Reporting goes first: it owns `sales_facts`, which deliberately has **no foreign keys** to
`stations`/`products`/`tanks` precisely so the table can be lifted into its own database
unchanged. Replace `SaleReportingListener` with a `@KafkaListener` on the same record shape.
The idempotency guard on `sale_reference` is what makes at-least-once delivery over a network
safe — it is already there.

Accounting follows the same way. Inventory goes last, because tank stock is the one read the
POS wants to be fresh.

### Step 3 — what actually gets harder

Being honest about the cost, since the current design pays none of it:

- **Ordering.** Partition by `station_id` so all events for a station stay ordered. Sales for
  different stations are genuinely independent.
- **Schema evolution.** The event record becomes a published contract. Additive fields only;
  a schema registry or explicit versioned topics.
- **Debugging.** Today `event_publication` answers "did accounting see sale X?" with one SQL
  query. Across services that needs distributed tracing.
- **Failed messages.** The registry's resubmit-on-restart becomes a dead-letter topic plus a
  replay tool.

None of this is worth paying for a single fuel-station deployment. The point of the current
design is that the *decision* stays open: the modules are separated by an event contract rather
than by method calls, so the split is a transport change, not a rewrite.

## Verified endpoints

| Method | Path | Permission |
| --- | --- | --- |
| `POST` | `/api/v1/sales` | `sale:create` |
| `GET` | `/api/v1/sales`, `/api/v1/sales/{id}` | `sale:read` |
| `GET` | `/api/v1/accounting/journal-entries` | `accounting:read` |
| `GET` | `/api/v1/accounting/trial-balance` | `accounting:read` |
| `GET` | `/api/v1/reports/sales/daily` | `report:read` |
| `GET` | `/api/v1/reports/sales/products` | `report:read` |
| `GET` | `/api/v1/reports/sales/attendants` | `report:read` |

An `ATTENDANT` role is bootstrapped with `sale:create`, `sale:read`, `product:read`, `tank:read`.
