import { useMutation } from "@tanstack/react-query";
import { login } from "../api/auth.api";
import { setTokens } from "../lib/token-storage";

export function useLogin() {
  return useMutation({
    mutationFn: login,
    onSuccess: (tokens) => {
      setTokens(tokens);
    },
  });
}
