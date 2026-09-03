import { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { DataTable } from "@/components/data-table";
import {
  DateRangeFilter,
  type DateRange,
} from "@/components/date-range-filter";
import { PageHeader } from "@/components/page-header";
import { TabNav } from "@/components/tab-nav";
import { Alert } from "@/components/ui/alert";
import { Select } from "@/components/ui/select";
import { StatCard } from "@/features/dashboard/components/stat-card";
import { getErrorMessage } from "@/features/auth/lib/get-error-message";
import { useActiveProducts } from "@/features/products/hooks/use-products";
import { useStationOptions } from "@/features/stations/hooks/use-stations";
import { daysAgoIsoDate, formatAmount, formatQuantity, toIsoDate } from "@/lib/format";
import {
  createAttendantColumns,
  createDailyColumns,
  createProductReportColumns,
} from "./report-columns";
import {
  useAttendantSales,
  useDailySales,
  useProductSales,
} from "../hooks/use-reports";

type ReportTab = "daily" | "products" | "attendants";

export function ReportsPage() {
  const { t } = useTranslation();
  const [tab, setTab] = useState<ReportTab>("daily");
  const [stationFilter, setStationFilter] = useState("");
  const [range, setRange] = useState<DateRange>({
    from: daysAgoIsoDate(30),
    to: toIsoDate(new Date()),
  });

  const { data: stations } = useStationOptions();
  const { data: products } = useActiveProducts();

  const filters = {
    stationId: stationFilter ? Number(stationFilter) : undefined,
    from: range.from || undefined,
    to: range.to || undefined,
  };

  const daily = useDailySales(filters, tab === "daily");
  const byProduct = useProductSales(filters, tab === "products");
  const byAttendant = useAttendantSales(filters, tab === "attendants");

  const active =
    tab === "daily" ? daily : tab === "products" ? byProduct : byAttendant;

  const totals = useMemo(() => {
    const rows = active.data ?? [];
    return rows.reduce(
      (acc, row) => ({
        quantity: acc.quantity + Number(row.quantity),
        amount: acc.amount + Number(row.totalAmount),
        transactions: acc.transactions + Number(row.transactions),
      }),
      { quantity: 0, amount: 0, transactions: 0 },
    );
  }, [active.data]);

  const maxAmount = useMemo(
    () =>
      (active.data ?? []).reduce(
        (max, row) => Math.max(max, Number(row.totalAmount)),
        0,
      ),
    [active.data],
  );

  const productNames = useMemo(
    () => new Map(products?.map((product) => [product.id, product.name])),
    [products],
  );

  const tabs: { id: ReportTab; label: string }[] = [
    { id: "daily", label: t("reports.tabs.daily") },
    { id: "products", label: t("reports.tabs.products") },
    { id: "attendants", label: t("reports.tabs.attendants") },
  ];

  return (
    <main className="flex flex-1 flex-col gap-6 p-6">
      <PageHeader title={t("reports.title")} subtitle={t("reports.subtitle")} />

      <div className="flex flex-wrap items-end gap-3">
        <div className="flex w-56 flex-col gap-1.5">
          <label className="font-jetbrains text-xs tracking-[0.15em] text-muted-foreground uppercase">
            {t("reports.filters.station")}
          </label>
          <Select
            value={stationFilter}
            onChange={(event) => setStationFilter(event.target.value)}
          >
            <option value="">{t("reports.filters.allStations")}</option>
            {stations?.map((station) => (
              <option key={station.id} value={station.id}>
                {station.name}
              </option>
            ))}
          </Select>
        </div>
        <DateRangeFilter value={range} onChange={setRange} />
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-3">
        <StatCard
          label={t("reports.totals.amount")}
          value={formatAmount(totals.amount)}
          hint={t("reports.totals.amountHint")}
        />
        <StatCard
          label={t("reports.totals.quantity")}
          value={formatQuantity(totals.quantity)}
          hint={t("reports.totals.quantityHint")}
        />
        <StatCard
          label={t("reports.totals.transactions")}
          value={String(totals.transactions)}
          hint={t("reports.totals.transactionsHint")}
        />
      </div>

      <TabNav tabs={tabs} active={tab} onChange={setTab} />

      {active.isError && (
        <Alert>
          <span>{getErrorMessage(active.error)}</span>
        </Alert>
      )}

      {tab === "daily" && (
        <DataTable
          data={daily.data ?? []}
          columns={createDailyColumns(t, maxAmount)}
          isLoading={daily.isLoading}
          emptyMessage={t("reports.noResults")}
          getRowId={(row) => row.businessDate}
        />
      )}

      {tab === "products" && (
        <DataTable
          data={byProduct.data ?? []}
          columns={createProductReportColumns(
            t,
            maxAmount,
            (id) => productNames.get(id) ?? `#${id}`,
          )}
          isLoading={byProduct.isLoading}
          emptyMessage={t("reports.noResults")}
          getRowId={(row) => String(row.productId)}
        />
      )}

      {tab === "attendants" && (
        <DataTable
          data={byAttendant.data ?? []}
          columns={createAttendantColumns(t, maxAmount)}
          isLoading={byAttendant.isLoading}
          emptyMessage={t("reports.noResults")}
          getRowId={(row) => row.attendant}
        />
      )}
    </main>
  );
}
