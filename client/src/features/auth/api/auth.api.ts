import { api } from "@/lib/axios";
import type { ApiResponse } from "@/lib/api-response";
import type { LoginCredentials, TokenPair } from "../types/auth";

export async function login(credentials: LoginCredentials): Promise<TokenPair> {
  const { data } = await api.post<ApiResponse<TokenPair>>(
    "/auth/login",
    credentials,
  );
  return data.data;
}
