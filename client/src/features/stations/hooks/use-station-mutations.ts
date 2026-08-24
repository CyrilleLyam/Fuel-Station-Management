import { useMutation, useQueryClient } from "@tanstack/react-query";
import {
  createStation,
  deleteStation,
  updateStation,
} from "../api/station.api";
import type { StationInput } from "../types/station";

export function useCreateStation() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: createStation,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["stations"] });
    },
  });
}

interface UpdateStationVars {
  id: number;
  input: StationInput & { enabled: boolean };
}

export function useUpdateStation() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ id, input }: UpdateStationVars) => updateStation(id, input),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["stations"] });
    },
  });
}

export function useDeleteStation() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: deleteStation,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["stations"] });
    },
  });
}
