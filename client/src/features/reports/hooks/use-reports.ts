import { keepPreviousData, useQuery } from "@tanstack/react-query";
import {
  getAttendantSales,
  getDailySales,
  getProductSales,
} from "../api/report.api";
import type { ReportFilters } from "../types/report";

export function useDailySales(filters: ReportFilters, enabled = true) {
  return useQuery({
    queryKey: ["reports", "daily", filters],
    queryFn: () => getDailySales(filters),
    placeholderData: keepPreviousData,
    enabled,
  });
}

export function useProductSales(filters: ReportFilters, enabled = true) {
  return useQuery({
    queryKey: ["reports", "products", filters],
    queryFn: () => getProductSales(filters),
    placeholderData: keepPreviousData,
    enabled,
  });
}

export function useAttendantSales(filters: ReportFilters, enabled = true) {
  return useQuery({
    queryKey: ["reports", "attendants", filters],
    queryFn: () => getAttendantSales(filters),
    placeholderData: keepPreviousData,
    enabled,
  });
}
