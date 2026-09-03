import { keepPreviousData, useQuery } from "@tanstack/react-query";
import { listTanks, type ListTanksParams } from "../api/tank.api";

export function useTanks(params: ListTanksParams) {
  return useQuery({
    queryKey: ["tanks", params],
    queryFn: () => listTanks(params),
    placeholderData: keepPreviousData,
  });
}

export function useTankOptions(stationId?: number) {
  return useQuery({
    queryKey: ["tanks", { stationId, size: 200 }],
    queryFn: () => listTanks({ stationId, size: 200 }),
    select: (page) => page.content.filter((tank) => tank.active),
  });
}
