import axios, { type CreateAxiosDefaults } from "axios";
import { toCamelCase, toSnakeCase } from "./case-convert";

export function createHttpClient(config?: CreateAxiosDefaults) {
  const instance = axios.create({
    baseURL: new URL(import.meta.env.VITE_API_URL).pathname,
    headers: {
      "Content-Type": "application/json",
    },
    ...config,
  });

  instance.interceptors.request.use((requestConfig) => {
    if (requestConfig.data) {
      requestConfig.data = toSnakeCase(requestConfig.data);
    }
    return requestConfig;
  });

  instance.interceptors.response.use((response) => {
    response.data = toCamelCase(response.data);
    return response;
  });

  return instance;
}
