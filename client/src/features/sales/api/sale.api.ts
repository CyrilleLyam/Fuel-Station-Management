import { api } from "@/lib/axios";
import type { ApiResponse, PageMeta } from "@/lib/api-response";
import type { RecordSaleInput, Sale } from "../types/sale";

export interface SalePage {
  content: Sale[];
  meta: PageMeta;
}

export interface ListSalesParams {
  stationId?: number;
  productId?: number;
  from?: string;
  to?: string;
  page?: number;
  size?: number;
}

export async function listSales(
  params: ListSalesParams = {},
): Promise<SalePage> {
  const { data } = await api.get<ApiResponse<Sale[]>>("/sales", {
    params: {
      stationId: params.stationId ?? undefined,
      productId: params.productId ?? undefined,
      from: params.from || undefined,
      to: params.to || undefined,
      page: params.page ?? 0,
      size: params.size ?? 20,
    },
  });
  return { content: data.data, meta: data.meta! };
}

export async function getSale(id: number): Promise<Sale> {
  const { data } = await api.get<ApiResponse<Sale>>(`/sales/${id}`);
  return data.data;
}

export async function recordSale(input: RecordSaleInput): Promise<Sale> {
  const { data } = await api.post<ApiResponse<Sale>>("/sales", input);
  return data.data;
}
