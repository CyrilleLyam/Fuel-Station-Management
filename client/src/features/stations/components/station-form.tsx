import { zodResolver } from "@hookform/resolvers/zod";
import { CircleAlert, Loader2 } from "lucide-react";
import type { ReactNode } from "react";
import { useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import { Alert } from "@/components/ui/alert";
import { Button } from "@/components/ui/button";
import {
  DialogClose,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { getErrorMessage } from "@/features/auth/lib/get-error-message";
import { useCreateStation, useUpdateStation } from "../hooks/use-station-mutations";
import { createStationFormSchema, type StationFormValues } from "../types/station";
import type { Station } from "../types/station";

export function StationForm({
  station,
  onSuccess,
}: {
  station?: Station;
  onSuccess: () => void;
}) {
  const { t } = useTranslation();
  const isEdit = Boolean(station);
  const createStation = useCreateStation();
  const updateStation = useUpdateStation();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<StationFormValues>({
    resolver: zodResolver(createStationFormSchema(t)),
    defaultValues: {
      name: station?.name ?? "",
      code: station?.code ?? "",
      address: station?.address ?? "",
      phone: station?.phone ?? "",
      latitude: station?.latitude != null ? String(station.latitude) : "",
      longitude: station?.longitude != null ? String(station.longitude) : "",
    },
  });

  const isPending = isEdit ? updateStation.isPending : createStation.isPending;
  const error = isEdit ? updateStation.error : createStation.error;

  function onSubmit(values: StationFormValues) {
    const payload = {
      name: values.name,
      code: values.code,
      address: values.address,
      phone: values.phone,
      latitude: values.latitude ? Number(values.latitude) : undefined,
      longitude: values.longitude ? Number(values.longitude) : undefined,
    };

    if (isEdit && station) {
      updateStation.mutate(
        { id: station.id, input: { ...payload, enabled: station.enabled } },
        { onSuccess },
      );
    } else {
      createStation.mutate(payload, { onSuccess });
    }
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col">
      <DialogHeader>
        <DialogTitle>
          {isEdit ? t("stations.form.editTitle") : t("stations.form.addTitle")}
        </DialogTitle>
        <DialogDescription>
          {isEdit
            ? t("stations.form.editDescription", { code: station?.code })
            : t("stations.form.addDescription")}
        </DialogDescription>
      </DialogHeader>

      <div className="grid grid-cols-1 gap-4 px-6 py-5 sm:grid-cols-2">
        <Field label={t("stations.form.name")} error={errors.name?.message}>
          <Input autoFocus {...register("name")} />
        </Field>
        <Field label={t("stations.form.code")} error={errors.code?.message}>
          <Input disabled={isEdit} {...register("code")} />
        </Field>
        <Field
          label={t("stations.form.address")}
          error={errors.address?.message}
          span
        >
          <Input {...register("address")} />
        </Field>
        <Field label={t("stations.form.phone")} error={errors.phone?.message}>
          <Input {...register("phone")} />
        </Field>
        <Field
          label={t("stations.form.latitude")}
          error={errors.latitude?.message}
        >
          <Input inputMode="decimal" {...register("latitude")} />
        </Field>
        <Field
          label={t("stations.form.longitude")}
          error={errors.longitude?.message}
        >
          <Input inputMode="decimal" {...register("longitude")} />
        </Field>
      </div>

      {error && (
        <div className="px-6 pb-4">
          <Alert>
            <CircleAlert className="mt-0.5 size-4 shrink-0" />
            <span>{getErrorMessage(error)}</span>
          </Alert>
        </div>
      )}

      <DialogFooter>
        <DialogClose
          className="inline-flex h-8 shrink-0 items-center justify-center rounded-lg border border-border bg-background px-2.5 text-sm font-medium hover:bg-muted"
        >
          {t("stations.form.cancel")}
        </DialogClose>
        <Button type="submit" disabled={isPending} className="relative">
          <span className={isPending ? "invisible" : undefined}>
            {isEdit
              ? t("stations.form.submitSave")
              : t("stations.form.submitAdd")}
          </span>
          {isPending && (
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
  span,
  children,
}: {
  label: string;
  error?: string;
  span?: boolean;
  children: ReactNode;
}) {
  return (
    <div className={span ? "flex flex-col gap-1.5 sm:col-span-2" : "flex flex-col gap-1.5"}>
      <label className="font-jetbrains text-xs tracking-[0.15em] text-muted-foreground uppercase">
        {label}
      </label>
      {children}
      {error && <p className="text-sm text-destructive">{error}</p>}
    </div>
  );
}
