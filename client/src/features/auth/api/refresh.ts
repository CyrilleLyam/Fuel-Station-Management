import type { ApiResponse } from "@/lib/api-response";
import { createHttpClient } from "@/lib/http-client";
import { getRefreshToken, setAccessToken } from "../lib/token-storage";

const refreshClient = createHttpClient();

interface AccessTokenResponse {
  accessToken: string;
}

export async function refreshAccessToken(): Promise<string> {
  const refreshToken = getRefreshToken();
  if (!refreshToken) {
    throw new Error("No refresh token available");
  }

  const { data } = await refreshClient.post<ApiResponse<AccessTokenResponse>>(
    "/auth/refresh",
    { refreshToken },
  );

  const accessToken = data.data.accessToken;
  setAccessToken(accessToken);
  return accessToken;
}
