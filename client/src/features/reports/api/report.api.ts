import { api } from "@/lib/axios";
import type { ApiResponse } from "@/lib/api-response";
import type {
  AttendantSalesRow,
  DailySalesRow,
  ProductSalesRow,
  ReportFilters,
} from "../types/report";

function toParams(filters: ReportFilters) {
  return {
    stationId: filters.stationId ?? undefined,
    from: filters.from || undefined,
    to: filters.to || undefined,
  };
}

export async function getDailySales(
  filters: ReportFilters,
): Promise<DailySalesRow[]> {
  const { data } = await api.get<ApiResponse<DailySalesRow[]>>(
    "/reports/sales/daily",
    { params: toParams(filters) },
  );
  return data.data;
}

export async function getProductSales(
  filters: ReportFilters,
): Promise<ProductSalesRow[]> {
  const { data } = await api.get<ApiResponse<ProductSalesRow[]>>(
    "/reports/sales/products",
    { params: toParams(filters) },
  );
  return data.data;
}

export async function getAttendantSales(
  filters: ReportFilters,
): Promise<AttendantSalesRow[]> {
  const { data } = await api.get<ApiResponse<AttendantSalesRow[]>>(
    "/reports/sales/attendants",
    { params: toParams(filters) },
  );
  return data.data;
}
