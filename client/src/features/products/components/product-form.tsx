import { zodResolver } from "@hookform/resolvers/zod";
import { CircleAlert, Loader2 } from "lucide-react";
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
import {
  useCreateProduct,
  useUpdateProduct,
} from "../hooks/use-product-mutations";
import {
  createProductFormSchema,
  FUEL_TYPES,
  type Product,
  type ProductFormValues,
} from "../types/product";

export function ProductForm({
  product,
  onSuccess,
}: {
  product?: Product;
  onSuccess: () => void;
}) {
  const { t } = useTranslation();
  const isEdit = Boolean(product);
  const createProduct = useCreateProduct();
  const updateProduct = useUpdateProduct();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ProductFormValues>({
    resolver: zodResolver(createProductFormSchema(t)),
    defaultValues: {
      name: product?.name ?? "",
      sku: product?.sku ?? "",
      fuelType: product?.fuelType ?? "REGULAR",
      unit: product?.unit ?? "L",
      unitPrice: product?.unitPrice != null ? String(product.unitPrice) : "0",
    },
  });

  const isPending = isEdit ? updateProduct.isPending : createProduct.isPending;
  const error = isEdit ? updateProduct.error : createProduct.error;

  function onSubmit(values: ProductFormValues) {
    if (isEdit && product) {
      updateProduct.mutate(
        {
          id: product.id,
          input: {
            name: values.name,
            fuelType: values.fuelType,
            unit: values.unit,
            active: product.active,
          },
        },
        { onSuccess },
      );
      return;
    }

    createProduct.mutate(
      {
        name: values.name,
        sku: values.sku,
        fuelType: values.fuelType,
        unit: values.unit,
        unitPrice: Number(values.unitPrice),
      },
      { onSuccess },
    );
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col">
      <DialogHeader>
        <DialogTitle>
          {isEdit ? t("products.form.editTitle") : t("products.form.addTitle")}
        </DialogTitle>
        <DialogDescription>
          {isEdit
            ? t("products.form.editDescription", { sku: product?.sku })
            : t("products.form.addDescription")}
        </DialogDescription>
      </DialogHeader>

      <div className="grid grid-cols-1 gap-4 px-6 py-5 sm:grid-cols-2">
        <FormField label={t("products.form.name")} error={errors.name?.message}>
          <Input autoFocus {...register("name")} />
        </FormField>
        <FormField label={t("products.form.sku")} error={errors.sku?.message}>
          <Input disabled={isEdit} {...register("sku")} />
        </FormField>
        <FormField
          label={t("products.form.fuelType")}
          error={errors.fuelType?.message}
        >
          <Select {...register("fuelType")}>
            {FUEL_TYPES.map((fuelType) => (
              <option key={fuelType} value={fuelType}>
                {t(`products.fuelTypes.${fuelType}`)}
              </option>
            ))}
          </Select>
        </FormField>
        <FormField label={t("products.form.unit")} error={errors.unit?.message}>
          <Input placeholder="L" {...register("unit")} />
        </FormField>
        {!isEdit && (
          <FormField
            label={t("products.form.unitPrice")}
            error={errors.unitPrice?.message}
          >
            <Input inputMode="decimal" {...register("unitPrice")} />
          </FormField>
        )}
      </div>

      {isEdit && (
        <p className="px-6 pb-4 text-xs text-muted-foreground">
          {t("products.form.priceHint")}
        </p>
      )}

      {error && (
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
            {isEdit ? t("common.saveChanges") : t("products.addProduct")}
          </span>
          {isPending && (
            <Loader2 className="absolute inset-0 m-auto size-4 animate-spin" />
          )}
        </Button>
      </DialogFooter>
    </form>
  );
}
