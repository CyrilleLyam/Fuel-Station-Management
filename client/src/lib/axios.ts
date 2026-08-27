import { AxiosError, type InternalAxiosRequestConfig } from "axios";
import { refreshAccessToken } from "@/features/auth/api/refresh";
import { clearTokens, getAccessToken } from "@/features/auth/lib/token-storage";
import i18n from "./i18n";
import { showToast } from "./toast";
import { createHttpClient } from "./http-client";

export const api = createHttpClient();

api.interceptors.request.use((config) => {
  const token = getAccessToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

let refreshPromise: Promise<string> | null = null;

interface RetriableConfig extends InternalAxiosRequestConfig {
  _retried?: boolean;
}

api.interceptors.response.use(
  (response) => response,
  async (error: AxiosError) => {
    const config = error.config as RetriableConfig | undefined;

    if (error.response?.status === 403) {
      showToast(i18n.t("errors.accessDenied"));
    }

    if (error.response?.status !== 401 || !config || config._retried) {
      if (error.response?.status === 401) {
        clearTokens();
      }
      return Promise.reject(error);
    }

    config._retried = true;

    try {
      refreshPromise ??= refreshAccessToken().finally(() => {
        refreshPromise = null;
      });
      const accessToken = await refreshPromise;
      config.headers.Authorization = `Bearer ${accessToken}`;
      return api(config);
    } catch (refreshError) {
      clearTokens();
      return Promise.reject(refreshError);
    }
  },
);
