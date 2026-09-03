import { keepPreviousData, useQuery } from "@tanstack/react-query";
import { listProducts, type ListProductsParams } from "../api/product.api";

export function useProducts(params: ListProductsParams) {
  return useQuery({
    queryKey: ["products", params],
    queryFn: () => listProducts(params),
    placeholderData: keepPreviousData,
  });
}

export function useActiveProducts() {
  return useQuery({
    queryKey: ["products", { size: 200 }],
    queryFn: () => listProducts({ size: 200 }),
    select: (page) => page.content.filter((product) => product.active),
  });
}
