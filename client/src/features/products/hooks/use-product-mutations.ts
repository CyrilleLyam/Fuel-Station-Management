import { useMutation, useQueryClient } from "@tanstack/react-query";
import {
  changeProductPrice,
  createProduct,
  deleteProduct,
  updateProduct,
} from "../api/product.api";
import type { UpdateProductInput } from "../types/product";

function useProductInvalidator() {
  const queryClient = useQueryClient();
  return () => {
    queryClient.invalidateQueries({ queryKey: ["products"] });
  };
}

export function useCreateProduct() {
  const invalidate = useProductInvalidator();
  return useMutation({
    mutationFn: createProduct,
    onSuccess: invalidate,
  });
}

interface UpdateProductVars {
  id: number;
  input: UpdateProductInput;
}

export function useUpdateProduct() {
  const invalidate = useProductInvalidator();
  return useMutation({
    mutationFn: ({ id, input }: UpdateProductVars) => updateProduct(id, input),
    onSuccess: invalidate,
  });
}

interface ChangePriceVars {
  id: number;
  unitPrice: number;
}

export function useChangeProductPrice() {
  const invalidate = useProductInvalidator();
  return useMutation({
    mutationFn: ({ id, unitPrice }: ChangePriceVars) =>
      changeProductPrice(id, unitPrice),
    onSuccess: invalidate,
  });
}

export function useDeleteProduct() {
  const invalidate = useProductInvalidator();
  return useMutation({
    mutationFn: deleteProduct,
    onSuccess: invalidate,
  });
}
