import { keepPreviousData, useQuery } from "@tanstack/react-query";
import { listStations, type ListStationsParams } from "../api/station.api";

export function useStations(params: ListStationsParams) {
  return useQuery({
    queryKey: ["stations", params],
    queryFn: () => listStations(params),
    placeholderData: keepPreviousData,
  });
}
