import { api } from "@/lib/axios";
import type { ApiResponse, PageMeta } from "@/lib/api-response";
import type { CreateTankInput, Tank, UpdateTankInput } from "../types/tank";

export interface TankPage {
  content: Tank[];
  meta: PageMeta;
}

export interface ListTanksParams {
  stationId?: number;
  keyword?: string;
  page?: number;
  size?: number;
}

export async function listTanks(
  params: ListTanksParams = {},
): Promise<TankPage> {
  const { data } = await api.get<ApiResponse<Tank[]>>("/tanks", {
    params: {
      stationId: params.stationId ?? undefined,
      keyword: params.keyword || undefined,
      page: params.page ?? 0,
      size: params.size ?? 20,
    },
  });
  return { content: data.data, meta: data.meta! };
}

export async function getTank(id: number): Promise<Tank> {
  const { data } = await api.get<ApiResponse<Tank>>(`/tanks/${id}`);
  return data.data;
}

export async function createTank(input: CreateTankInput): Promise<Tank> {
  const { data } = await api.post<ApiResponse<Tank>>("/tanks", input);
  return data.data;
}

export async function updateTank(
  id: number,
  input: UpdateTankInput,
): Promise<Tank> {
  const { data } = await api.put<ApiResponse<Tank>>(`/tanks/${id}`, input);
  return data.data;
}

export async function assignTankProduct(
  id: number,
  productId: number,
): Promise<Tank> {
  const { data } = await api.patch<ApiResponse<Tank>>(`/tanks/${id}/product`, {
    productId,
  });
  return data.data;
}

export async function setTankCapacity(
  id: number,
  capacity: number,
): Promise<Tank> {
  const { data } = await api.patch<ApiResponse<Tank>>(`/tanks/${id}/capacity`, {
    capacity,
  });
  return data.data;
}

export async function recordTankDelivery(
  id: number,
  amount: number,
): Promise<Tank> {
  const { data } = await api.post<ApiResponse<Tank>>(
    `/tanks/${id}/deliveries`,
    { amount },
  );
  return data.data;
}

export async function recordTankDispense(
  id: number,
  amount: number,
): Promise<Tank> {
  const { data } = await api.post<ApiResponse<Tank>>(`/tanks/${id}/dispenses`, {
    amount,
  });
  return data.data;
}

export async function deleteTank(id: number): Promise<void> {
  await api.delete(`/tanks/${id}`);
}
