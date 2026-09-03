import { useMemo } from "react";
import { Link } from "@tanstack/react-router";
import { useTranslation } from "react-i18next";
import { DataTable } from "@/components/data-table";
import { PageHeader } from "@/components/page-header";
import { usePermissions } from "@/features/iam/permissions/use-permissions";
import { useActiveProducts } from "@/features/products/hooks/use-products";
import { createSaleColumns } from "@/features/sales/components/sale-columns";
import { useStationOptions } from "@/features/stations/hooks/use-stations";
import { formatAmount, formatQuantity } from "@/lib/format";
import { StatCard } from "./stat-card";
import {
  useProductSummary,
  useRecentSales,
  useStationSummary,
  useTankSummary,
  useTodaySales,
} from "../hooks/use-dashboard";

export function DashboardPage() {
  const { t } = useTranslation();
  const { can } = usePermissions();

  const canReadReports = can("report", "read");
  const canReadTanks = can("tank", "read");
  const canReadStations = can("station", "read");
  const canReadProducts = can("product", "read");
  const canReadSales = can("sale", "read");

  const todaySales = useTodaySales(canReadReports);
  const tanks = useTankSummary(canReadTanks);
  const stations = useStationSummary(canReadStations);
  const products = useProductSummary(canReadProducts);
  const recentSales = useRecentSales(canReadSales);

  const { data: stationOptions } = useStationOptions();
  const { data: productOptions } = useActiveProducts();

  const stationNames = useMemo(
    () => new Map(stationOptions?.map((station) => [station.id, station.name])),
    [stationOptions],
  );
  const productNames = useMemo(
    () => new Map(productOptions?.map((product) => [product.id, product.name])),
    [productOptions],
  );

  const saleColumns = createSaleColumns(t, {
    stationName: (id) => stationNames.get(id) ?? `#${id}`,
    productName: (id) => productNames.get(id) ?? `#${id}`,
  });

  return (
    <main className="flex flex-1 flex-col gap-6 p-6">
      <PageHeader
        title={t("dashboard.title")}
        subtitle={t("dashboard.subtitle")}
      />

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-4">
        <StatCard
          label={t("dashboard.stats.todaySales.label")}
          value={
            canReadReports ? formatAmount(todaySales.data?.amount ?? 0) : "—"
          }
          hint={
            canReadReports
              ? t("dashboard.stats.todaySales.hint", {
                  count: todaySales.data?.transactions ?? 0,
                })
              : t("dashboard.stats.noAccess")
          }
        />
        <StatCard
          label={t("dashboard.stats.fuelInStock.label")}
          value={canReadTanks ? formatQuantity(tanks.data?.inStock ?? 0) : "—"}
          hint={
            canReadTanks
              ? t("dashboard.stats.fuelInStock.hint", {
                  count: tanks.data?.lowCount ?? 0,
                })
              : t("dashboard.stats.noAccess")
          }
        />
        <StatCard
          label={t("dashboard.stats.stations.label")}
          value={canReadStations ? String(stations.data?.total ?? 0) : "—"}
          hint={
            canReadStations
              ? t("dashboard.stats.stations.hint", {
                  count: stations.data?.enabled ?? 0,
                })
              : t("dashboard.stats.noAccess")
          }
        />
        <StatCard
          label={t("dashboard.stats.products.label")}
          value={canReadProducts ? String(products.data?.total ?? 0) : "—"}
          hint={
            canReadProducts
              ? t("dashboard.stats.products.hint", {
                  count: products.data?.active ?? 0,
                })
              : t("dashboard.stats.noAccess")
          }
        />
      </div>

      {canReadSales && (
        <section className="flex flex-col gap-3">
          <div className="flex items-center justify-between">
            <h2 className="font-display text-lg font-semibold tracking-tight">
              {t("dashboard.recentSales.title")}
            </h2>
            <Link
              to="/sales"
              className="text-sm font-medium text-primary hover:underline"
            >
              {t("dashboard.recentSales.viewAll")}
            </Link>
          </div>
          <DataTable
            data={recentSales.data ?? []}
            columns={saleColumns}
            isLoading={recentSales.isLoading}
            emptyMessage={t("dashboard.recentSales.empty")}
            getRowId={(row) => String(row.id)}
          />
        </section>
      )}
    </main>
  );
}
