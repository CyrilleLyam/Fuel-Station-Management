import { zodResolver } from "@hookform/resolvers/zod";
import { CircleAlert, Loader2 } from "lucide-react";
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
import { FormField } from "@/components/form-field";
import { getErrorMessage } from "@/features/auth/lib/get-error-message";
import { formatAmount } from "@/lib/format";
import { useChangeProductPrice } from "../hooks/use-product-mutations";
import {
  createPriceFormSchema,
  type PriceFormValues,
  type Product,
} from "../types/product";

export function ProductPriceDialog({
  product,
  open,
  onOpenChange,
}: {
  product?: Product;
  open: boolean;
  onOpenChange: (open: boolean) => void;
}) {
  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-md gap-0 p-0">
        {open && product && (
          <PriceForm product={product} onSuccess={() => onOpenChange(false)} />
        )}
      </DialogContent>
    </Dialog>
  );
}

function PriceForm({
  product,
  onSuccess,
}: {
  product: Product;
  onSuccess: () => void;
}) {
  const { t } = useTranslation();
  const changePrice = useChangeProductPrice();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<PriceFormValues>({
    resolver: zodResolver(createPriceFormSchema(t)),
    defaultValues: { unitPrice: String(product.unitPrice) },
  });

  function onSubmit(values: PriceFormValues) {
    changePrice.mutate(
      { id: product.id, unitPrice: Number(values.unitPrice) },
      { onSuccess },
    );
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col">
      <DialogHeader>
        <DialogTitle>{t("products.price.title")}</DialogTitle>
        <DialogDescription>
          {t("products.price.description", {
            name: product.name,
            price: formatAmount(product.unitPrice),
          })}
        </DialogDescription>
      </DialogHeader>

      <div className="px-6 py-5">
        <FormField
          label={t("products.form.unitPrice")}
          error={errors.unitPrice?.message}
        >
          <Input autoFocus inputMode="decimal" {...register("unitPrice")} />
        </FormField>
      </div>

      {changePrice.error && (
        <div className="px-6 pb-4">
          <Alert>
            <CircleAlert className="mt-0.5 size-4 shrink-0" />
            <span>{getErrorMessage(changePrice.error)}</span>
          </Alert>
        </div>
      )}

      <DialogFooter>
        <DialogClose className="inline-flex h-8 shrink-0 items-center justify-center rounded-lg border border-border bg-background px-2.5 text-sm font-medium hover:bg-muted">
          {t("common.cancel")}
        </DialogClose>
        <Button
          type="submit"
          disabled={changePrice.isPending}
          className="relative"
        >
          <span className={changePrice.isPending ? "invisible" : undefined}>
            {t("products.price.submit")}
          </span>
          {changePrice.isPending && (
            <Loader2 className="absolute inset-0 m-auto size-4 animate-spin" />
          )}
        </Button>
      </DialogFooter>
    </form>
  );
}
