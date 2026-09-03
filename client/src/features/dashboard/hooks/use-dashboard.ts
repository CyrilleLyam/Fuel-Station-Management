import { useQuery } from "@tanstack/react-query";
import { listProducts } from "@/features/products/api/product.api";
import { getDailySales } from "@/features/reports/api/report.api";
import { listSales } from "@/features/sales/api/sale.api";
import { listStations } from "@/features/stations/api/station.api";
import { listTanks } from "@/features/tanks/api/tank.api";
import { endOfDayIso, startOfDayIso, toIsoDate } from "@/lib/format";

export function useTodaySales(enabled: boolean) {
  const today = toIsoDate(new Date());
  return useQuery({
    queryKey: ["reports", "daily", { from: today, to: today }],
    queryFn: () => getDailySales({ from: today, to: today }),
    select: (rows) =>
      rows.reduce(
        (totals, row) => ({
          amount: totals.amount + Number(row.totalAmount),
          quantity: totals.quantity + Number(row.quantity),
          transactions: totals.transactions + Number(row.transactions),
        }),
        { amount: 0, quantity: 0, transactions: 0 },
      ),
    enabled,
  });
}

export function useTankSummary(enabled: boolean) {
  return useQuery({
    queryKey: ["tanks", { size: 200 }],
    queryFn: () => listTanks({ size: 200 }),
    select: (page) => ({
      count: page.content.length,
      lowCount: page.content.filter(
        (tank) =>
          Number(tank.capacity) > 0 &&
          Number(tank.currentQuantity) / Number(tank.capacity) < 0.15,
      ).length,
      inStock: page.content.reduce(
        (total, tank) => total + Number(tank.currentQuantity),
        0,
      ),
    }),
    enabled,
  });
}

export function useStationSummary(enabled: boolean) {
  return useQuery({
    queryKey: ["stations", { size: 200 }],
    queryFn: () => listStations({ size: 200 }),
    select: (page) => ({
      total: page.meta.totalElements,
      enabled: page.content.filter((station) => station.enabled).length,
    }),
    enabled,
  });
}

export function useProductSummary(enabled: boolean) {
  return useQuery({
    queryKey: ["products", { size: 200 }],
    queryFn: () => listProducts({ size: 200 }),
    select: (page) => ({
      total: page.meta.totalElements,
      active: page.content.filter((product) => product.active).length,
    }),
    enabled,
  });
}

export function useRecentSales(enabled: boolean) {
  const today = toIsoDate(new Date());
  return useQuery({
    queryKey: [
      "sales",
      { from: startOfDayIso(today), to: endOfDayIso(today), size: 5 },
    ],
    queryFn: () =>
      listSales({
        from: startOfDayIso(today),
        to: endOfDayIso(today),
        page: 0,
        size: 5,
      }),
    select: (page) => page.content,
    enabled,
  });
}
