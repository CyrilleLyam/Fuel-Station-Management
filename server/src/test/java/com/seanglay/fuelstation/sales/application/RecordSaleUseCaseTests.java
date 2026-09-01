package com.seanglay.fuelstation.sales.application;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

import com.seanglay.fuelstation.product.ProductCatalog;
import com.seanglay.fuelstation.product.ProductSnapshot;
import com.seanglay.fuelstation.sales.PaymentMethod;
import com.seanglay.fuelstation.sales.SaleCompleted;
import com.seanglay.fuelstation.sales.domain.Sale;
import com.seanglay.fuelstation.sales.domain.SaleRepository;
import com.seanglay.fuelstation.shared.domain.CursorPageResult;
import com.seanglay.fuelstation.shared.domain.NotFoundException;
import com.seanglay.fuelstation.shared.domain.PageResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

class RecordSaleUseCaseTests {

	private static final ProductSnapshot DIESEL = new ProductSnapshot(3L, "Diesel", "FUEL-DIESEL", "L",
			new BigDecimal("1.150"), true);

	private final RecordingEventPublisher publisher = new RecordingEventPublisher();

	private final StubProductCatalog catalog = new StubProductCatalog();

	private RecordSaleUseCase useCase;

	@BeforeEach
	void setUp() {
		useCase = new RecordSaleUseCase(new StubSaleRepository(), catalog, publisher);
	}

	@Test
	void pricesTheSaleFromTheCatalogRatherThanTheCaller() {
		catalog.snapshot = DIESEL;

		Sale sale = useCase.execute(1L, 2L, 3L, "attendant", new BigDecimal("40.000"), PaymentMethod.CASH,
				Instant.parse("2026-09-01T08:00:00Z"));

		assertThat(sale.getUnitPrice()).isEqualByComparingTo("1.150");
		assertThat(sale.getTotalAmount()).isEqualByComparingTo("46.00");
	}

	@Test
	void publishesOneSaleCompletedEventCarryingEverythingConsumersNeed() {
		catalog.snapshot = DIESEL;

		Sale sale = useCase.execute(1L, 2L, 3L, "attendant", new BigDecimal("40.000"), PaymentMethod.CARD,
				Instant.parse("2026-09-01T08:00:00Z"));

		assertThat(publisher.events).hasSize(1);
		assertThat(publisher.events.getFirst()).isInstanceOf(SaleCompleted.class);

		SaleCompleted event = (SaleCompleted) publisher.events.getFirst();
		assertThat(event.reference()).isEqualTo(sale.getReference());
		assertThat(event.saleId()).isEqualTo(sale.getId());
		assertThat(event.stationId()).isEqualTo(1L);
		assertThat(event.tankId()).isEqualTo(2L);
		assertThat(event.productId()).isEqualTo(3L);
		assertThat(event.attendant()).isEqualTo("attendant");
		assertThat(event.quantity()).isEqualByComparingTo("40.000");
		assertThat(event.unitPrice()).isEqualByComparingTo("1.150");
		assertThat(event.totalAmount()).isEqualByComparingTo("46.00");
		assertThat(event.paymentMethod()).isEqualTo(PaymentMethod.CARD);
		assertThat(event.occurredAt()).isEqualTo(Instant.parse("2026-09-01T08:00:00Z"));
	}

	@Test
	void refusesAnUnknownProductWithoutPublishing() {
		catalog.snapshot = null;

		assertThatExceptionOfType(NotFoundException.class)
			.isThrownBy(() -> useCase.execute(1L, 2L, 99L, "attendant", BigDecimal.ONE, PaymentMethod.CASH, null));
		assertThat(publisher.events).isEmpty();
	}

	@Test
	void refusesADeactivatedProductWithoutPublishing() {
		catalog.snapshot = new ProductSnapshot(3L, "Diesel", "FUEL-DIESEL", "L", new BigDecimal("1.150"), false);

		assertThatIllegalStateException()
			.isThrownBy(() -> useCase.execute(1L, 2L, 3L, "attendant", BigDecimal.ONE, PaymentMethod.CASH, null))
			.withMessageContaining("FUEL-DIESEL");
		assertThat(publisher.events).isEmpty();
	}

	private static final class RecordingEventPublisher implements ApplicationEventPublisher {

		private final List<Object> events = new ArrayList<>();

		@Override
		public void publishEvent(ApplicationEvent event) {
			events.add(event);
		}

		@Override
		public void publishEvent(Object event) {
			events.add(event);
		}

	}

	private static final class StubProductCatalog implements ProductCatalog {

		private ProductSnapshot snapshot;

		@Override
		public Optional<ProductSnapshot> findById(Long id) {
			return Optional.ofNullable(snapshot);
		}

	}

	private static final class StubSaleRepository implements SaleRepository {

		@Override
		public Optional<Sale> findById(Long id) {
			return Optional.empty();
		}

		@Override
		public PageResult<Sale> search(Long stationId, Long productId, Instant from, Instant to, int page, int size) {
			return new PageResult<>(List.of(), page, size, 0, 0);
		}

		@Override
		public CursorPageResult<Sale> searchAfter(Long stationId, Long productId, Instant from, Instant to, Long cursor,
				int size) {
			return new CursorPageResult<>(List.of(), null, false);
		}

		@Override
		public Sale save(Sale sale) {
			return sale;
		}

	}

}
