import { keepPreviousData, useQuery } from "@tanstack/react-query";
import { listStations, type ListStationsParams } from "../api/station.api";

export function useStations(params: ListStationsParams) {
  return useQuery({
    queryKey: ["stations", params],
    queryFn: () => listStations(params),
    placeholderData: keepPreviousData,
  });
}

export function useStationOptions() {
  return useQuery({
    queryKey: ["stations", { size: 200 }],
    queryFn: () => listStations({ size: 200 }),
    select: (page) => page.content,
    staleTime: 5 * 60 * 1000,
  });
}
