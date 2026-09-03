import { zodResolver } from "@hookform/resolvers/zod";
import { CircleAlert, Loader2 } from "lucide-react";
import { useEffect } from "react";
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
import { useTankOptions } from "@/features/tanks/hooks/use-tanks";
import { formatAmount, formatQuantity } from "@/lib/format";
import { useRecordSale } from "../hooks/use-sale-mutations";
import {
  createSaleFormSchema,
  PAYMENT_METHODS,
  type SaleFormValues,
} from "../types/sale";

export function SaleForm({ onSuccess }: { onSuccess: () => void }) {
  const { t } = useTranslation();
  const { data: stations } = useStationOptions();
  const { data: products } = useActiveProducts();
  const recordSale = useRecordSale();

  const {
    register,
    handleSubmit,
    watch,
    setValue,
    formState: { errors },
  } = useForm<SaleFormValues>({
    resolver: zodResolver(createSaleFormSchema(t)),
    defaultValues: {
      stationId: "",
      tankId: "",
      productId: "",
      quantity: "",
      paymentMethod: "CASH",
      soldAt: "",
    },
  });

  const stationId = watch("stationId");
  const tankId = watch("tankId");
  const productId = watch("productId");
  const quantity = watch("quantity");

  const { data: tanks } = useTankOptions(
    stationId ? Number(stationId) : undefined,
  );

  const selectedTank = tanks?.find((tank) => String(tank.id) === tankId);
  const selectedProduct = products?.find(
    (product) => String(product.id) === productId,
  );

  useEffect(() => {
    if (selectedTank?.productId != null) {
      setValue("productId", String(selectedTank.productId));
    }
  }, [selectedTank, setValue]);

  const estimatedTotal =
    selectedProduct && quantity && !Number.isNaN(Number(quantity))
      ? Number(selectedProduct.unitPrice) * Number(quantity)
      : null;

  function onSubmit(values: SaleFormValues) {
    recordSale.mutate(
      {
        stationId: Number(values.stationId),
        tankId: Number(values.tankId),
        productId: Number(values.productId),
        quantity: Number(values.quantity),
        paymentMethod: values.paymentMethod,
        soldAt: values.soldAt
          ? new Date(values.soldAt).toISOString()
          : undefined,
      },
      { onSuccess },
    );
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col">
      <DialogHeader>
        <DialogTitle>{t("sales.form.title")}</DialogTitle>
        <DialogDescription>{t("sales.form.description")}</DialogDescription>
      </DialogHeader>

      <div className="grid grid-cols-1 gap-4 px-6 py-5 sm:grid-cols-2">
        <FormField
          label={t("sales.form.station")}
          error={errors.stationId?.message}
        >
          <Select
            {...register("stationId", {
              onChange: () => {
                setValue("tankId", "");
                setValue("productId", "");
              },
            })}
          >
            <option value="">{t("sales.form.selectStation")}</option>
            {stations?.map((station) => (
              <option key={station.id} value={station.id}>
                {station.name}
              </option>
            ))}
          </Select>
        </FormField>

        <FormField label={t("sales.form.tank")} error={errors.tankId?.message}>
          <Select disabled={!stationId} {...register("tankId")}>
            <option value="">{t("sales.form.selectTank")}</option>
            {tanks?.map((tank) => (
              <option key={tank.id} value={tank.id}>
                {tank.label} · {formatQuantity(tank.currentQuantity)}
              </option>
            ))}
          </Select>
        </FormField>

        <FormField
          label={t("sales.form.product")}
          error={errors.productId?.message}
        >
          <Select {...register("productId")}>
            <option value="">{t("sales.form.selectProduct")}</option>
            {products?.map((product) => (
              <option key={product.id} value={product.id}>
                {product.name}
              </option>
            ))}
          </Select>
        </FormField>

        <FormField
          label={t("sales.form.quantity")}
          error={errors.quantity?.message}
        >
          <Input inputMode="decimal" {...register("quantity")} />
        </FormField>

        <FormField
          label={t("sales.form.paymentMethod")}
          error={errors.paymentMethod?.message}
        >
          <Select {...register("paymentMethod")}>
            {PAYMENT_METHODS.map((method) => (
              <option key={method} value={method}>
                {t(`sales.paymentMethods.${method}`)}
              </option>
            ))}
          </Select>
        </FormField>

        <FormField
          label={t("sales.form.soldAt")}
          error={errors.soldAt?.message}
        >
          <Input type="datetime-local" {...register("soldAt")} />
        </FormField>
      </div>

      {estimatedTotal != null && (
        <div className="flex items-center justify-between border-t border-border px-6 py-3 text-sm">
          <span className="font-jetbrains text-xs tracking-[0.15em] text-muted-foreground uppercase">
            {t("sales.form.estimatedTotal")}
          </span>
          <span className="font-jetbrains font-medium">
            {formatAmount(estimatedTotal)}
          </span>
        </div>
      )}

      {recordSale.error && (
        <div className="px-6 pb-4">
          <Alert>
            <CircleAlert className="mt-0.5 size-4 shrink-0" />
            <span>{getErrorMessage(recordSale.error)}</span>
          </Alert>
        </div>
      )}

      <DialogFooter>
        <DialogClose className="inline-flex h-8 shrink-0 items-center justify-center rounded-lg border border-border bg-background px-2.5 text-sm font-medium hover:bg-muted">
          {t("common.cancel")}
        </DialogClose>
        <Button
          type="submit"
          disabled={recordSale.isPending}
          className="relative"
        >
          <span className={recordSale.isPending ? "invisible" : undefined}>
            {t("sales.form.submit")}
          </span>
          {recordSale.isPending && (
            <Loader2 className="absolute inset-0 m-auto size-4 animate-spin" />
          )}
        </Button>
      </DialogFooter>
    </form>
  );
}
