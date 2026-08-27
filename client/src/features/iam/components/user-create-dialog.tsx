import { zodResolver } from "@hookform/resolvers/zod";
import { CircleAlert, Loader2 } from "lucide-react";
import type { ReactNode } from "react";
import { useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import { Alert } from "@/components/ui/alert";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { getErrorMessage } from "@/features/auth/lib/get-error-message";
import { showToast } from "@/lib/toast";
import { useCreateIamUser } from "../hooks/use-iam-mutations";
import { createUserSchema, type CreateUserValues } from "../types/iam";

export function UserCreateDialog({
  open,
  onOpenChange,
}: {
  open: boolean;
  onOpenChange: (open: boolean) => void;
}) {
  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-md gap-0 p-0">
        {open && <UserCreateForm onSuccess={() => onOpenChange(false)} />}
      </DialogContent>
    </Dialog>
  );
}

function UserCreateForm({ onSuccess }: { onSuccess: () => void }) {
  const { t } = useTranslation();
  const createUser = useCreateIamUser();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<CreateUserValues>({
    resolver: zodResolver(createUserSchema(t)),
    defaultValues: { username: "", email: "", password: "" },
  });

  function onSubmit(values: CreateUserValues) {
    createUser.mutate(values, {
      onSuccess: () => {
        showToast(t("iam.users.created"), "success");
        onSuccess();
      },
    });
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col">
      <DialogHeader>
        <DialogTitle>{t("iam.users.form.title")}</DialogTitle>
        <DialogDescription>{t("iam.users.form.description")}</DialogDescription>
      </DialogHeader>

      <div className="flex flex-col gap-4 px-6 py-5">
        <Field
          label={t("iam.users.form.username")}
          error={errors.username?.message}
        >
          <Input autoFocus {...register("username")} />
        </Field>
        <Field label={t("iam.users.form.email")} error={errors.email?.message}>
          <Input type="email" {...register("email")} />
        </Field>
        <Field
          label={t("iam.users.form.password")}
          error={errors.password?.message}
        >
          <Input type="password" {...register("password")} />
        </Field>
      </div>

      {createUser.error && (
        <div className="px-6 pb-4">
          <Alert>
            <CircleAlert className="mt-0.5 size-4 shrink-0" />
            <span>{getErrorMessage(createUser.error)}</span>
          </Alert>
        </div>
      )}

      <DialogFooter>
        <DialogClose className="inline-flex h-8 shrink-0 items-center justify-center rounded-lg border border-border bg-background px-2.5 text-sm font-medium hover:bg-muted">
          {t("common.cancel")}
        </DialogClose>
        <Button type="submit" disabled={createUser.isPending} className="relative">
          <span className={createUser.isPending ? "invisible" : undefined}>
            {t("iam.users.form.submit")}
          </span>
          {createUser.isPending && (
            <Loader2 className="absolute inset-0 m-auto size-4 animate-spin" />
          )}
        </Button>
      </DialogFooter>
    </form>
  );
}

function Field({
  label,
  error,
  children,
}: {
  label: string;
  error?: string;
  children: ReactNode;
}) {
  return (
    <div className="flex flex-col gap-1.5">
      <label className="font-jetbrains text-xs tracking-[0.15em] text-muted-foreground uppercase">
        {label}
      </label>
      {children}
      {error && <p className="text-sm text-destructive">{error}</p>}
    </div>
  );
}
