import { z } from "zod";

export interface Station {
  id: number;
  name: string;
  code: string;
  address: string | null;
  phone: string | null;
  latitude: number | null;
  longitude: number | null;
  enabled: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface StationFormValues {
  name: string;
  code: string;
  address?: string;
  phone?: string;
  latitude?: string;
  longitude?: string;
}

export function createStationFormSchema(t: (key: string) => string) {
  return z.object({
    name: z.string().min(1, t("stations.form.nameRequired")),
    code: z.string().min(1, t("stations.form.codeRequired")),
    address: z.string().optional(),
    phone: z.string().optional(),
    latitude: z.string().optional(),
    longitude: z.string().optional(),
  });
}

export interface StationInput {
  name: string;
  code: string;
  address?: string;
  phone?: string;
  latitude?: number;
  longitude?: number;
}
