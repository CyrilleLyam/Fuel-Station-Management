import { useMemo, useState } from "react";
import type { SortingState } from "@tanstack/react-table";
import { Plus } from "lucide-react";
import { useTranslation } from "react-i18next";
import { DataTable } from "@/components/data-table";
import {
  DateRangeFilter,
  type DateRange,
} from "@/components/date-range-filter";
import { PageHeader } from "@/components/page-header";
import { PaginationBar } from "@/components/pagination-bar";
import { Alert } from "@/components/ui/alert";
import { Button } from "@/components/ui/button";
import { Select } from "@/components/ui/select";
import { getErrorMessage } from "@/features/auth/lib/get-error-message";
import { usePermissions } from "@/features/iam/permissions/use-permissions";
import { useActiveProducts } from "@/features/products/hooks/use-products";
import { useStationOptions } from "@/features/stations/hooks/use-stations";
import {
  daysAgoIsoDate,
  endOfDayIso,
  formatAmount,
  formatQuantity,
  startOfDayIso,
  toIsoDate,
} from "@/lib/format";
import { createSaleColumns } from "./sale-columns";
import { SaleDialog } from "./sale-dialog";
import { useSales } from "../hooks/use-sales";

const PAGE_SIZE = 10;

export function SalesPage() {
  const { t } = useTranslation();
  const { can } = usePermissions();
  const canCreate = can("sale", "create");
  const [page, setPage] = useState(0);
  const [stationFilter, setStationFilter] = useState("");
  const [productFilter, setProductFilter] = useState("");
  const [range, setRange] = useState<DateRange>({
    from: daysAgoIsoDate(30),
    to: toIsoDate(new Date()),
  });
  const [sorting, setSorting] = useState<SortingState>([]);
  const [dialogOpen, setDialogOpen] = useState(false);

  const { data: stations } = useStationOptions();
  const { data: products } = useActiveProducts();

  const { data, isLoading, isError, error, isFetching } = useSales({
    stationId: stationFilter ? Number(stationFilter) : undefined,
    productId: productFilter ? Number(productFilter) : undefined,
    from: range.from ? startOfDayIso(range.from) : undefined,
    to: range.to ? endOfDayIso(range.to) : undefined,
    page,
    size: PAGE_SIZE,
  });

  const stationNames = useMemo(
    () => new Map(stations?.map((station) => [station.id, station.name])),
    [stations],
  );
  const productNames = useMemo(
    () => new Map(products?.map((product) => [product.id, product.name])),
    [products],
  );

  const columns = createSaleColumns(t, {
    stationName: (id) => stationNames.get(id) ?? `#${id}`,
    productName: (id) => productNames.get(id) ?? `#${id}`,
  });

  const pageTotals = useMemo(() => {
    const rows = data?.content ?? [];
    return rows.reduce(
      (totals, sale) => ({
        quantity: totals.quantity + Number(sale.quantity),
        amount: totals.amount + Number(sale.totalAmount),
      }),
      { quantity: 0, amount: 0 },
    );
  }, [data]);

  return (
    <main className="flex flex-1 flex-col gap-6 p-6">
      <PageHeader
        title={t("sales.title")}
        subtitle={t("sales.subtitle")}
        actions={
          canCreate && (
            <Button onClick={() => setDialogOpen(true)} className="gap-1.5">
              <Plus className="size-4" />
              {t("sales.recordSale")}
            </Button>
          )
        }
      />

      <div className="flex flex-wrap items-end gap-3">
        <div className="flex w-56 flex-col gap-1.5">
          <label className="font-jetbrains text-xs tracking-[0.15em] text-muted-foreground uppercase">
            {t("sales.filters.station")}
          </label>
          <Select
            value={stationFilter}
            onChange={(event) => {
              setStationFilter(event.target.value);
              setPage(0);
            }}
          >
            <option value="">{t("sales.filters.allStations")}</option>
            {stations?.map((station) => (
              <option key={station.id} value={station.id}>
                {station.name}
              </option>
            ))}
          </Select>
        </div>
        <div className="flex w-56 flex-col gap-1.5">
          <label className="font-jetbrains text-xs tracking-[0.15em] text-muted-foreground uppercase">
            {t("sales.filters.product")}
          </label>
          <Select
            value={productFilter}
            onChange={(event) => {
              setProductFilter(event.target.value);
              setPage(0);
            }}
          >
            <option value="">{t("sales.filters.allProducts")}</option>
            {products?.map((product) => (
              <option key={product.id} value={product.id}>
                {product.name}
              </option>
            ))}
          </Select>
        </div>
        <DateRangeFilter
          value={range}
          onChange={(next) => {
            setRange(next);
            setPage(0);
          }}
        />
      </div>

      {isError && (
        <Alert>
          <span>{getErrorMessage(error)}</span>
        </Alert>
      )}

      <DataTable
        data={data?.content ?? []}
        columns={columns}
        isLoading={isLoading}
        emptyMessage={t("sales.noResults")}
        sorting={sorting}
        onSortingChange={setSorting}
        getRowId={(row) => String(row.id)}
      />

      {(data?.content.length ?? 0) > 0 && (
        <div className="flex flex-wrap gap-6 rounded-xl border border-border bg-muted/30 px-4 py-3 text-sm">
          <span className="text-muted-foreground">
            {t("sales.pageTotals.quantity")}{" "}
            <span className="font-jetbrains font-medium text-foreground">
              {formatQuantity(pageTotals.quantity)}
            </span>
          </span>
          <span className="text-muted-foreground">
            {t("sales.pageTotals.amount")}{" "}
            <span className="font-jetbrains font-medium text-foreground">
              {formatAmount(pageTotals.amount)}
            </span>
          </span>
        </div>
      )}

      {data?.meta && (
        <PaginationBar
          meta={data.meta}
          isFetching={isFetching}
          countLabel={t("sales.saleCount", { count: data.meta.totalElements })}
          onPageChange={setPage}
        />
      )}

      <SaleDialog open={dialogOpen} onOpenChange={setDialogOpen} />
    </main>
  );
}
