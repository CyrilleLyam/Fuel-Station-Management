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
import { formatQuantity } from "@/lib/format";
import {
  useRecordTankDelivery,
  useRecordTankDispense,
} from "../hooks/use-tank-mutations";
import {
  createAmountFormSchema,
  type AmountFormValues,
  type Tank,
  type TankMovementKind,
} from "../types/tank";

export function TankMovementDialog({
  tank,
  kind,
  open,
  onOpenChange,
}: {
  tank?: Tank;
  kind: TankMovementKind;
  open: boolean;
  onOpenChange: (open: boolean) => void;
}) {
  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-md gap-0 p-0">
        {open && tank && (
          <MovementForm
            tank={tank}
            kind={kind}
            onSuccess={() => onOpenChange(false)}
          />
        )}
      </DialogContent>
    </Dialog>
  );
}

function MovementForm({
  tank,
  kind,
  onSuccess,
}: {
  tank: Tank;
  kind: TankMovementKind;
  onSuccess: () => void;
}) {
  const { t } = useTranslation();
  const delivery = useRecordTankDelivery();
  const dispense = useRecordTankDispense();
  const mutation = kind === "delivery" ? delivery : dispense;

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<AmountFormValues>({
    resolver: zodResolver(
      createAmountFormSchema(
        t,
        "tanks.movement.amountRequired",
        "tanks.movement.amountInvalid",
      ),
    ),
    defaultValues: { amount: "" },
  });

  function onSubmit(values: AmountFormValues) {
    mutation.mutate({ id: tank.id, amount: Number(values.amount) }, { onSuccess });
  }

  const limit =
    kind === "delivery" ? tank.availableSpace : tank.currentQuantity;

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col">
      <DialogHeader>
        <DialogTitle>
          {kind === "delivery"
            ? t("tanks.movement.deliveryTitle")
            : t("tanks.movement.dispenseTitle")}
        </DialogTitle>
        <DialogDescription>
          {kind === "delivery"
            ? t("tanks.movement.deliveryDescription", {
                label: tank.label,
                available: formatQuantity(limit),
              })
            : t("tanks.movement.dispenseDescription", {
                label: tank.label,
                available: formatQuantity(limit),
              })}
        </DialogDescription>
      </DialogHeader>

      <div className="px-6 py-5">
        <FormField
          label={t("tanks.movement.amount")}
          error={errors.amount?.message}
        >
          <Input autoFocus inputMode="decimal" {...register("amount")} />
        </FormField>
      </div>

      {mutation.error && (
        <div className="px-6 pb-4">
          <Alert>
            <CircleAlert className="mt-0.5 size-4 shrink-0" />
            <span>{getErrorMessage(mutation.error)}</span>
          </Alert>
        </div>
      )}

      <DialogFooter>
        <DialogClose className="inline-flex h-8 shrink-0 items-center justify-center rounded-lg border border-border bg-background px-2.5 text-sm font-medium hover:bg-muted">
          {t("common.cancel")}
        </DialogClose>
        <Button type="submit" disabled={mutation.isPending} className="relative">
          <span className={mutation.isPending ? "invisible" : undefined}>
            {kind === "delivery"
              ? t("tanks.movement.submitDelivery")
              : t("tanks.movement.submitDispense")}
          </span>
          {mutation.isPending && (
            <Loader2 className="absolute inset-0 m-auto size-4 animate-spin" />
          )}
        </Button>
      </DialogFooter>
    </form>
  );
}
