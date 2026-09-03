import { api } from "@/lib/axios";
import type { ApiResponse, PageMeta } from "@/lib/api-response";
import type { Station, StationInput } from "../types/station";

export interface StationPage {
  content: Station[];
  meta: PageMeta;
}

export interface ListStationsParams {
  keyword?: string;
  page?: number;
  size?: number;
}

export async function listStations(
  params: ListStationsParams = {},
): Promise<StationPage> {
  const { data } = await api.get<ApiResponse<Station[]>>("/stations", {
    params: {
      keyword: params.keyword || undefined,
      page: params.page ?? 0,
      size: params.size ?? 20,
    },
  });
  return { content: data.data, meta: data.meta! };
}

export async function getStation(id: number): Promise<Station> {
  const { data } = await api.get<ApiResponse<Station>>(`/stations/${id}`);
  return data.data;
}

export async function createStation(input: StationInput): Promise<Station> {
  const { data } = await api.post<ApiResponse<Station>>("/stations", input);
  return data.data;
}

export async function updateStation(
  id: number,
  input: StationInput & { enabled: boolean },
): Promise<Station> {
  const { data } = await api.put<ApiResponse<Station>>(
    `/stations/${id}`,
    input,
  );
  return data.data;
}

export async function deleteStation(id: number): Promise<void> {
  await api.delete(`/stations/${id}`);
}
