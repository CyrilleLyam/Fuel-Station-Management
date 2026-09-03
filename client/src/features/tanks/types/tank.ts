import { z } from "zod";

export interface Tank {
  id: number;
  stationId: number;
  productId: number | null;
  label: string;
  capacity: number;
  currentQuantity: number;
  availableSpace: number;
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface CreateTankInput {
  stationId: number;
  label: string;
  capacity: number;
  productId?: number;
}

export interface UpdateTankInput {
  label: string;
  active: boolean;
}

export type TankMovementKind = "delivery" | "dispense";

export interface TankFormValues {
  stationId: string;
  label: string;
  capacity: string;
  productId: string;
}

export function createTankFormSchema(t: (key: string) => string) {
  return z.object({
    stationId: z.string().min(1, t("tanks.form.stationRequired")),
    label: z.string().min(1, t("tanks.form.labelRequired")),
    capacity: z
      .string()
      .min(1, t("tanks.form.capacityRequired"))
      .refine(
        (value) => !Number.isNaN(Number(value)) && Number(value) > 0,
        t("tanks.form.capacityInvalid"),
      ),
    productId: z.string(),
  });
}

export interface AmountFormValues {
  amount: string;
}

export function createAmountFormSchema(
  t: (key: string) => string,
  requiredKey: string,
  invalidKey: string,
) {
  return z.object({
    amount: z
      .string()
      .min(1, t(requiredKey))
      .refine(
        (value) => !Number.isNaN(Number(value)) && Number(value) > 0,
        t(invalidKey),
      ),
  });
}
