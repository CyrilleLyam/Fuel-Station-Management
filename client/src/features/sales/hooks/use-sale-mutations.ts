import { useMutation, useQueryClient } from "@tanstack/react-query";
import { recordSale } from "../api/sale.api";

export function useRecordSale() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: recordSale,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["sales"] });
      queryClient.invalidateQueries({ queryKey: ["tanks"] });
      queryClient.invalidateQueries({ queryKey: ["reports"] });
      queryClient.invalidateQueries({ queryKey: ["accounting"] });
    },
  });
}
