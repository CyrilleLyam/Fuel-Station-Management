import { useNavigate } from "@tanstack/react-router";
import { zodResolver } from "@hookform/resolvers/zod";
import { CircleAlert, Loader2 } from "lucide-react";
import { useForm } from "react-hook-form";
import { Alert } from "@/components/ui/alert";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useLogin } from "../hooks/use-login";
import { getErrorMessage } from "../lib/get-error-message";
import { loginSchema, type LoginCredentials } from "../types/auth";

export function LoginForm({ redirectTo }: { redirectTo?: string }) {
  const navigate = useNavigate();
  const { mutate, isPending, error } = useLogin();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginCredentials>({
    resolver: zodResolver(loginSchema),
    defaultValues: { username: "", password: "" },
  });

  function onSubmit(credentials: LoginCredentials) {
    mutate(credentials, {
      onSuccess: () => {
        if (redirectTo) {
          navigate({ href: redirectTo });
        } else {
          navigate({ to: "/" });
        }
      },
    });
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col gap-5">
      <div className="flex flex-col gap-1.5">
        <label
          htmlFor="username"
          className="font-jetbrains text-xs tracking-[0.15em] text-muted-foreground uppercase"
        >
          Username
        </label>
        <Input
          id="username"
          autoComplete="username"
          {...register("username")}
        />
        {errors.username && (
          <p className="text-sm text-destructive">
            {errors.username.message}
          </p>
        )}
      </div>
      <div className="flex flex-col gap-1.5">
        <label
          htmlFor="password"
          className="font-jetbrains text-xs tracking-[0.15em] text-muted-foreground uppercase"
        >
          Password
        </label>
        <Input
          id="password"
          type="password"
          autoComplete="current-password"
          {...register("password")}
        />
        {errors.password && (
          <p className="text-sm text-destructive">
            {errors.password.message}
          </p>
        )}
      </div>
      {error && (
        <Alert>
          <CircleAlert className="mt-0.5 size-4 shrink-0" />
          <span>{getErrorMessage(error)}</span>
        </Alert>
      )}
      <Button type="submit" disabled={isPending} className="relative mt-2">
        <span className={isPending ? "invisible" : undefined}>Sign in</span>
        {isPending && (
          <Loader2 className="absolute inset-0 m-auto size-4 animate-spin" />
        )}
      </Button>
    </form>
  );
}
