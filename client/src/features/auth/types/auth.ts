import { z } from "zod";

export interface LoginCredentials {
  username: string;
  password: string;
}

export function createLoginSchema(t: (key: string) => string) {
  return z.object({
    username: z.string().min(1, t("auth.login.usernameRequired")),
    password: z.string().min(1, t("auth.login.passwordRequired")),
  });
}

export interface TokenPair {
  accessToken: string;
  refreshToken: string;
}

export interface Permission {
  resource: string;
  action: string;
}

export interface CurrentUser {
  id: string;
  username: string;
  email: string;
  roles: string[];
  permissions: Permission[];
}
