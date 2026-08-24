import { api } from "@/lib/axios";
import type { ApiResponse } from "@/lib/api-response";
import type { CurrentUser } from "../types/auth";

export async function getCurrentUser(): Promise<CurrentUser> {
  const { data } = await api.get<ApiResponse<CurrentUser>>("/users/me");
  return data.data;
}
