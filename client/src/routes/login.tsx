import { createFileRoute, redirect } from "@tanstack/react-router";
import { z } from "zod";
import { useTranslation } from "react-i18next";
import { LanguageToggle } from "@/components/language-toggle";
import { ThemeToggle } from "@/components/theme-toggle";
import {
  AuthBrandPanel,
  MobileAuthBrandBar,
} from "@/features/auth/components/auth-brand-panel";
import { LoginForm } from "@/features/auth/components/login-form";
import { restoreSession } from "@/features/auth/lib/session";

const loginSearchSchema = z.object({
  redirect: z.string().optional(),
});

export const Route = createFileRoute("/login")({
  validateSearch: loginSearchSchema,
  beforeLoad: async () => {
    if (await restoreSession()) {
      throw redirect({ to: "/" });
    }
  },
  component: LoginComponent,
});

function LoginComponent() {
  const { t } = useTranslation();
  const { redirect: redirectTo } = Route.useSearch();

  return (
    <div className="grid min-h-svh lg:grid-cols-[minmax(0,42%)_minmax(0,58%)]">
      <AuthBrandPanel />
      <div className="relative flex flex-col">
        <div className="absolute top-6 right-6 flex items-center gap-2">
          <LanguageToggle />
          <ThemeToggle />
        </div>
        <MobileAuthBrandBar />
        <div className="flex flex-1 flex-col items-center justify-center px-6 py-16">
          <div className="flex w-full max-w-sm flex-col gap-8">
            <div className="flex flex-col gap-1.5">
              <span className="font-jetbrains text-xs tracking-[0.25em] text-muted-foreground uppercase">
                {t("auth.login.eyebrow")}
              </span>
              <h2 className="text-2xl font-semibold">
                {t("auth.login.title")}
              </h2>
              <p className="text-sm text-muted-foreground">
                {t("auth.login.subtitle")}
              </p>
            </div>
            <LoginForm redirectTo={redirectTo} />
          </div>
        </div>
      </div>
    </div>
  );
}
