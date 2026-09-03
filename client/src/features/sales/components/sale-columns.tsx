import { createColumnHelper, type ColumnDef } from "@tanstack/react-table";
import type { TFunction } from "i18next";
import { Badge } from "@/components/ui/badge";
import { formatAmount, formatDateTime, formatQuantity } from "@/lib/format";
import type { Sale } from "../types/sale";

const columnHelper = createColumnHelper<Sale>();

export function createSaleColumns(
  t: TFunction,
  lookups: {
    stationName: (id: number) => string;
    productName: (id: number) => string;
  },
): ColumnDef<Sale, any>[] {
  return [
    columnHelper.accessor("soldAt", {
      header: t("sales.columns.soldAt"),
      cell: (info) => (
        <span className="whitespace-nowrap">
          {formatDateTime(info.getValue())}
        </span>
      ),
    }),
    columnHelper.accessor("reference", {
      header: t("sales.columns.reference"),
      cell: (info) => (
        <span className="font-jetbrains text-xs text-muted-foreground">
          {String(info.getValue()).slice(0, 8)}
        </span>
      ),
    }),
    columnHelper.accessor("stationId", {
      header: t("sales.columns.station"),
      cell: (info) => (
        <span className="text-muted-foreground">
          {lookups.stationName(info.getValue())}
        </span>
      ),
    }),
    columnHelper.accessor("productId", {
      header: t("sales.columns.product"),
      cell: (info) => (
        <span className="font-medium">
          {lookups.productName(info.getValue())}
        </span>
      ),
    }),
    columnHelper.accessor("attendant", {
      header: t("sales.columns.attendant"),
      cell: (info) => (
        <span className="text-muted-foreground">{info.getValue()}</span>
      ),
    }),
    columnHelper.accessor("quantity", {
      header: t("sales.columns.quantity"),
      cell: (info) => (
        <span className="font-jetbrains">
          {formatQuantity(info.getValue())}
        </span>
      ),
    }),
    columnHelper.accessor("unitPrice", {
      header: t("sales.columns.unitPrice"),
      cell: (info) => (
        <span className="font-jetbrains text-muted-foreground">
          {formatAmount(info.getValue())}
        </span>
      ),
    }),
    columnHelper.accessor("totalAmount", {
      header: t("sales.columns.total"),
      cell: (info) => (
        <span className="font-jetbrains font-medium">
          {formatAmount(info.getValue())}
        </span>
      ),
    }),
    columnHelper.accessor("paymentMethod", {
      header: t("sales.columns.paymentMethod"),
      cell: (info) => (
        <Badge variant="outline">
          {t(`sales.paymentMethods.${info.getValue()}`)}
        </Badge>
      ),
    }),
  ];
}
