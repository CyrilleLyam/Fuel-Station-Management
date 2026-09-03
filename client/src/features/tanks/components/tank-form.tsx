import { zodResolver } from "@hookform/resolvers/zod";
import { CircleAlert, Loader2 } from "lucide-react";
import { useState } from "react";
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
import { Select } from "@/components/ui/select";
import { FormField } from "@/components/form-field";
import { getErrorMessage } from "@/features/auth/lib/get-error-message";
import { useActiveProducts } from "@/features/products/hooks/use-products";
import { useStationOptions } from "@/features/stations/hooks/use-stations";
import {
  useAssignTankProduct,
  useCreateTank,
  useSetTankCapacity,
  useUpdateTank,
} from "../hooks/use-tank-mutations";
import {
  createTankFormSchema,
  type Tank,
  type TankFormValues,
} from "../types/tank";

export function TankForm({
  tank,
  onSuccess,
}: {
  tank?: Tank;
  onSuccess: () => void;
}) {
  const { t } = useTranslation();
  const isEdit = Boolean(tank);
  const { data: stations } = useStationOptions();
  const { data: products } = useActiveProducts();
  const createTank = useCreateTank();
  const updateTank = useUpdateTank();
  const setCapacity = useSetTankCapacity();
  const assignProduct = useAssignTankProduct();
  const [submitError, setSubmitError] = useState<unknown>(null);
  const [isSaving, setIsSaving] = useState(false);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<TankFormValues>({
    resolver: zodResolver(createTankFormSchema(t)),
    defaultValues: {
      stationId: tank ? String(tank.stationId) : "",
      label: tank?.label ?? "",
      capacity: tank ? String(tank.capacity) : "",
      productId: tank?.productId != null ? String(tank.productId) : "",
    },
  });

  async function onSubmit(values: TankFormValues) {
    setSubmitError(null);

    if (!isEdit) {
      createTank.mutate(
        {
          stationId: Number(values.stationId),
          label: values.label,
          capacity: Number(values.capacity),
          productId: values.productId ? Number(values.productId) : undefined,
        },
        { onSuccess, onError: setSubmitError },
      );
      return;
    }

    if (!tank) {
      return;
    }

    setIsSaving(true);
    try {
      await updateTank.mutateAsync({
        id: tank.id,
        input: { label: values.label, active: tank.active },
      });

      if (Number(values.capacity) !== Number(tank.capacity)) {
        await setCapacity.mutateAsync({
          id: tank.id,
          capacity: Number(values.capacity),
        });
      }

      const nextProductId = values.productId ? Number(values.productId) : null;
      if (nextProductId !== null && nextProductId !== tank.productId) {
        await assignProduct.mutateAsync({
          id: tank.id,
          productId: nextProductId,
        });
      }

      onSuccess();
    } catch (error) {
      setSubmitError(error);
    } finally {
      setIsSaving(false);
    }
  }

  const isPending = isEdit ? isSaving : createTank.isPending;
  const error = submitError ?? (isEdit ? null : createTank.error);

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col">
      <DialogHeader>
        <DialogTitle>
          {isEdit ? t("tanks.form.editTitle") : t("tanks.form.addTitle")}
        </DialogTitle>
        <DialogDescription>
          {isEdit
            ? t("tanks.form.editDescription", { label: tank?.label })
            : t("tanks.form.addDescription")}
        </DialogDescription>
      </DialogHeader>

      <div className="grid grid-cols-1 gap-4 px-6 py-5 sm:grid-cols-2">
        <FormField
          label={t("tanks.form.station")}
          error={errors.stationId?.message}
        >
          <Select disabled={isEdit} {...register("stationId")}>
            <option value="">{t("tanks.form.selectStation")}</option>
            {stations?.map((station) => (
              <option key={station.id} value={station.id}>
                {station.name}
              </option>
            ))}
          </Select>
        </FormField>
        <FormField label={t("tanks.form.label")} error={errors.label?.message}>
          <Input autoFocus {...register("label")} />
        </FormField>
        <FormField
          label={t("tanks.form.capacity")}
          error={errors.capacity?.message}
        >
          <Input inputMode="decimal" {...register("capacity")} />
        </FormField>
        <FormField
          label={t("tanks.form.product")}
          error={errors.productId?.message}
        >
          <Select {...register("productId")}>
            <option value="">{t("tanks.form.noProduct")}</option>
            {products?.map((product) => (
              <option key={product.id} value={product.id}>
                {product.name}
              </option>
            ))}
          </Select>
        </FormField>
      </div>

      {error != null && (
        <div className="px-6 pb-4">
          <Alert>
            <CircleAlert className="mt-0.5 size-4 shrink-0" />
            <span>{getErrorMessage(error)}</span>
          </Alert>
        </div>
      )}

      <DialogFooter>
        <DialogClose className="inline-flex h-8 shrink-0 items-center justify-center rounded-lg border border-border bg-background px-2.5 text-sm font-medium hover:bg-muted">
          {t("common.cancel")}
        </DialogClose>
        <Button type="submit" disabled={isPending} className="relative">
          <span className={isPending ? "invisible" : undefined}>
            {isEdit ? t("common.saveChanges") : t("tanks.addTank")}
          </span>
          {isPending && (
            <Loader2 className="absolute inset-0 m-auto size-4 animate-spin" />
          )}
        </Button>
      </DialogFooter>
    </form>
  );
}
