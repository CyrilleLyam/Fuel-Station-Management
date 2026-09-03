import { z } from "zod";

export const FUEL_TYPES = [
  "DIESEL",
  "REGULAR",
  "PREMIUM",
  "LUBRICANT",
] as const;

export type FuelType = (typeof FUEL_TYPES)[number];

export interface Product {
  id: number;
  name: string;
  sku: string;
  fuelType: FuelType;
  unit: string;
  unitPrice: number;
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface CreateProductInput {
  name: string;
  sku: string;
  fuelType: FuelType;
  unit: string;
  unitPrice: number;
}

export interface UpdateProductInput {
  name: string;
  fuelType: FuelType;
  unit: string;
  active: boolean;
}

export interface ProductFormValues {
  name: string;
  sku: string;
  fuelType: FuelType;
  unit: string;
  unitPrice: string;
}

export function createProductFormSchema(t: (key: string) => string) {
  return z.object({
    name: z.string().min(1, t("products.form.nameRequired")),
    sku: z.string().min(1, t("products.form.skuRequired")),
    fuelType: z.enum(FUEL_TYPES),
    unit: z.string().min(1, t("products.form.unitRequired")),
    unitPrice: z
      .string()
      .min(1, t("products.form.priceRequired"))
      .refine(
        (value) => !Number.isNaN(Number(value)) && Number(value) >= 0,
        t("products.form.priceInvalid"),
      ),
  });
}

export interface PriceFormValues {
  unitPrice: string;
}

export function createPriceFormSchema(t: (key: string) => string) {
  return z.object({
    unitPrice: z
      .string()
      .min(1, t("products.form.priceRequired"))
      .refine(
        (value) => !Number.isNaN(Number(value)) && Number(value) >= 0,
        t("products.form.priceInvalid"),
      ),
  });
}
