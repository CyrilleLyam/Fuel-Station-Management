import { api } from "@/lib/axios";
import type { ApiResponse, PageMeta } from "@/lib/api-response";
import type {
  CreateProductInput,
  Product,
  UpdateProductInput,
} from "../types/product";

export interface ProductPage {
  content: Product[];
  meta: PageMeta;
}

export interface ListProductsParams {
  keyword?: string;
  page?: number;
  size?: number;
}

export async function listProducts(
  params: ListProductsParams = {},
): Promise<ProductPage> {
  const { data } = await api.get<ApiResponse<Product[]>>("/products", {
    params: {
      keyword: params.keyword || undefined,
      page: params.page ?? 0,
      size: params.size ?? 20,
    },
  });
  return { content: data.data, meta: data.meta! };
}

export async function getProduct(id: number): Promise<Product> {
  const { data } = await api.get<ApiResponse<Product>>(`/products/${id}`);
  return data.data;
}

export async function createProduct(
  input: CreateProductInput,
): Promise<Product> {
  const { data } = await api.post<ApiResponse<Product>>("/products", input);
  return data.data;
}

export async function updateProduct(
  id: number,
  input: UpdateProductInput,
): Promise<Product> {
  const { data } = await api.put<ApiResponse<Product>>(
    `/products/${id}`,
    input,
  );
  return data.data;
}

export async function changeProductPrice(
  id: number,
  unitPrice: number,
): Promise<Product> {
  const { data } = await api.patch<ApiResponse<Product>>(
    `/products/${id}/price`,
    { unitPrice },
  );
  return data.data;
}

export async function deleteProduct(id: number): Promise<void> {
  await api.delete(`/products/${id}`);
}
