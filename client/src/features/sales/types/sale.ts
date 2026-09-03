import { z } from "zod";

export const PAYMENT_METHODS = ["CASH", "CARD", "MOBILE", "CREDIT"] as const;

export type PaymentMethod = (typeof PAYMENT_METHODS)[number];

export interface Sale {
  id: number;
  reference: string;
  stationId: number;
  tankId: number;
  productId: number;
  attendant: string;
  quantity: number;
  unitPrice: number;
  totalAmount: number;
  paymentMethod: PaymentMethod;
  soldAt: string;
  createdAt: string;
  updatedAt: string;
}

export interface RecordSaleInput {
  stationId: number;
  tankId: number;
  productId: number;
  quantity: number;
  paymentMethod: PaymentMethod;
  soldAt?: string;
}

export interface SaleFormValues {
  stationId: string;
  tankId: string;
  productId: string;
  quantity: string;
  paymentMethod: PaymentMethod;
  soldAt: string;
}

export function createSaleFormSchema(t: (key: string) => string) {
  return z.object({
    stationId: z.string().min(1, t("sales.form.stationRequired")),
    tankId: z.string().min(1, t("sales.form.tankRequired")),
    productId: z.string().min(1, t("sales.form.productRequired")),
    quantity: z
      .string()
      .min(1, t("sales.form.quantityRequired"))
      .refine(
        (value) => !Number.isNaN(Number(value)) && Number(value) > 0,
        t("sales.form.quantityInvalid"),
      ),
    paymentMethod: z.enum(PAYMENT_METHODS),
    soldAt: z.string(),
  });
}
