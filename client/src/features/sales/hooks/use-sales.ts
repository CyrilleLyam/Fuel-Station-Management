import { keepPreviousData, useQuery } from "@tanstack/react-query";
import { listSales, type ListSalesParams } from "../api/sale.api";

export function useSales(params: ListSalesParams) {
  return useQuery({
    queryKey: ["sales", params],
    queryFn: () => listSales(params),
    placeholderData: keepPreviousData,
  });
}
