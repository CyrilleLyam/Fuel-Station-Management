import Cookies from "js-cookie";

const REFRESH_TOKEN_COOKIE = "refreshToken";
const REFRESH_TOKEN_EXPIRES_DAYS = 7;

let accessToken: string | null = null;

export interface Tokens {
  accessToken: string;
  refreshToken: string;
}

export function getAccessToken(): string | null {
  return accessToken;
}

export function setAccessToken(token: string | null): void {
  accessToken = token;
}

export function getRefreshToken(): string | null {
  return Cookies.get(REFRESH_TOKEN_COOKIE) ?? null;
}

function setRefreshToken(token: string): void {
  Cookies.set(REFRESH_TOKEN_COOKIE, token, {
    expires: REFRESH_TOKEN_EXPIRES_DAYS,
    sameSite: "strict",
    secure: window.location.protocol === "https:",
  });
}

export function setTokens(tokens: Tokens): void {
  setAccessToken(tokens.accessToken);
  setRefreshToken(tokens.refreshToken);
}

export function clearTokens(): void {
  accessToken = null;
  Cookies.remove(REFRESH_TOKEN_COOKIE);
}
