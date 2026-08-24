import { refreshAccessToken } from "../api/refresh";
import { clearTokens, getAccessToken, getRefreshToken } from "./token-storage";

export async function restoreSession(): Promise<boolean> {
  if (getAccessToken()) {
    return true;
  }
  if (!getRefreshToken()) {
    return false;
  }
  try {
    await refreshAccessToken();
    return true;
  } catch {
    clearTokens();
    return false;
  }
}
