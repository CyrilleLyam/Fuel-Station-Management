import { createColumnHelper, type ColumnDef } from "@tanstack/react-table";
import type { TFunction } from "i18next";
import { formatAmount, formatDate, formatQuantity } from "@/lib/format";
import type {
  AttendantSalesRow,
  DailySalesRow,
  ProductSalesRow,
} from "../types/report";

const dailyHelper = createColumnHelper<DailySalesRow>();
const productHelper = createColumnHelper<ProductSalesRow>();
const attendantHelper = createColumnHelper<AttendantSalesRow>();

function shareBar(value: number, max: number) {
  const ratio = max > 0 ? Math.min(value / max, 1) : 0;
  return (
    <div className="h-1.5 w-24 overflow-hidden rounded-full bg-muted">
      <div
        className="h-full rounded-full bg-primary"
        style={{ width: `${ratio * 100}%` }}
      />
    </div>
  );
}

export function createDailyColumns(
  t: TFunction,
  maxAmount: number,
): ColumnDef<DailySalesRow, any>[] {
  return [
    dailyHelper.accessor("businessDate", {
      header: t("reports.columns.date"),
      cell: (info) => (
        <span className="whitespace-nowrap font-medium">
          {formatDate(info.getValue())}
        </span>
      ),
    }),
    dailyHelper.accessor("transactions", {
      header: t("reports.columns.transactions"),
      cell: (info) => (
        <span className="font-jetbrains text-muted-foreground">
          {info.getValue()}
        </span>
      ),
    }),
    dailyHelper.accessor("quantity", {
      header: t("reports.columns.quantity"),
      cell: (info) => (
        <span className="font-jetbrains">
          {formatQuantity(info.getValue())}
        </span>
      ),
    }),
    dailyHelper.accessor("totalAmount", {
      header: t("reports.columns.amount"),
      cell: (info) => (
        <div className="flex items-center gap-3">
          <span className="font-jetbrains font-medium">
            {formatAmount(info.getValue())}
          </span>
          {shareBar(Number(info.getValue()), maxAmount)}
        </div>
      ),
    }),
  ];
}

export function createProductReportColumns(
  t: TFunction,
  maxAmount: number,
  productName: (id: number) => string,
): ColumnDef<ProductSalesRow, any>[] {
  return [
    productHelper.accessor("productId", {
      header: t("reports.columns.product"),
      cell: (info) => (
        <span className="font-medium">{productName(info.getValue())}</span>
      ),
    }),
    productHelper.accessor("transactions", {
      header: t("reports.columns.transactions"),
      cell: (info) => (
        <span className="font-jetbrains text-muted-foreground">
          {info.getValue()}
        </span>
      ),
    }),
    productHelper.accessor("quantity", {
      header: t("reports.columns.quantity"),
      cell: (info) => (
        <span className="font-jetbrains">
          {formatQuantity(info.getValue())}
        </span>
      ),
    }),
    productHelper.accessor("totalAmount", {
      header: t("reports.columns.amount"),
      cell: (info) => (
        <div className="flex items-center gap-3">
          <span className="font-jetbrains font-medium">
            {formatAmount(info.getValue())}
          </span>
          {shareBar(Number(info.getValue()), maxAmount)}
        </div>
      ),
    }),
  ];
}

export function createAttendantColumns(
  t: TFunction,
  maxAmount: number,
): ColumnDef<AttendantSalesRow, any>[] {
  return [
    attendantHelper.accessor("attendant", {
      header: t("reports.columns.attendant"),
      cell: (info) => <span className="font-medium">{info.getValue()}</span>,
    }),
    attendantHelper.accessor("transactions", {
      header: t("reports.columns.transactions"),
      cell: (info) => (
        <span className="font-jetbrains text-muted-foreground">
          {info.getValue()}
        </span>
      ),
    }),
    attendantHelper.accessor("quantity", {
      header: t("reports.columns.quantity"),
      cell: (info) => (
        <span className="font-jetbrains">
          {formatQuantity(info.getValue())}
        </span>
      ),
    }),
    attendantHelper.accessor("totalAmount", {
      header: t("reports.columns.amount"),
      cell: (info) => (
        <div className="flex items-center gap-3">
          <span className="font-jetbrains font-medium">
            {formatAmount(info.getValue())}
          </span>
          {shareBar(Number(info.getValue()), maxAmount)}
        </div>
      ),
    }),
  ];
}
