import { createColumnHelper, type ColumnDef } from "@tanstack/react-table";
import type { TFunction } from "i18next";
import { formatAmount, formatDate } from "@/lib/format";
import type {
  AccountBalance,
  JournalEntry,
  JournalLine,
} from "../types/accounting";

const entryHelper = createColumnHelper<JournalEntry>();
const balanceHelper = createColumnHelper<AccountBalance>();

export function createJournalEntryColumns(
  t: TFunction,
  stationName: (id: number) => string,
): ColumnDef<JournalEntry, any>[] {
  return [
    entryHelper.accessor("entryDate", {
      header: t("accounting.columns.date"),
      cell: (info) => (
        <span className="whitespace-nowrap font-medium">
          {formatDate(info.getValue())}
        </span>
      ),
    }),
    entryHelper.accessor("reference", {
      header: t("accounting.columns.reference"),
      cell: (info) => (
        <span className="font-jetbrains text-xs text-muted-foreground">
          {info.getValue()}
        </span>
      ),
    }),
    entryHelper.accessor("stationId", {
      header: t("accounting.columns.station"),
      cell: (info) => (
        <span className="text-muted-foreground">
          {stationName(info.getValue())}
        </span>
      ),
    }),
    entryHelper.accessor("memo", {
      header: t("accounting.columns.memo"),
      cell: (info) => (
        <span className="text-muted-foreground">{info.getValue() || "—"}</span>
      ),
    }),
    entryHelper.accessor("lines", {
      header: t("accounting.columns.lines"),
      cell: (info) => (
        <div className="flex flex-col gap-0.5">
          {(info.getValue() as JournalLine[]).map((line, index) => (
            <div
              key={`${line.account}-${index}`}
              className="flex items-baseline gap-2 whitespace-nowrap text-xs"
            >
              <span className="font-jetbrains text-muted-foreground">
                {t(`accounting.accounts.${line.account}`)}
              </span>
              <span className="font-jetbrains">
                {Number(line.debit) > 0
                  ? `${t("accounting.debitShort")} ${formatAmount(line.debit)}`
                  : `${t("accounting.creditShort")} ${formatAmount(line.credit)}`}
              </span>
            </div>
          ))}
        </div>
      ),
    }),
    entryHelper.accessor("totalDebit", {
      header: t("accounting.columns.debit"),
      cell: (info) => (
        <span className="font-jetbrains">{formatAmount(info.getValue())}</span>
      ),
    }),
    entryHelper.accessor("totalCredit", {
      header: t("accounting.columns.credit"),
      cell: (info) => (
        <span className="font-jetbrains">{formatAmount(info.getValue())}</span>
      ),
    }),
  ];
}

export function createTrialBalanceColumns(
  t: TFunction,
): ColumnDef<AccountBalance, any>[] {
  return [
    balanceHelper.accessor("account", {
      header: t("accounting.columns.account"),
      cell: (info) => (
        <span className="font-medium">
          {t(`accounting.accounts.${info.getValue()}`)}
        </span>
      ),
    }),
    balanceHelper.accessor("debit", {
      header: t("accounting.columns.debit"),
      cell: (info) => (
        <span className="font-jetbrains">{formatAmount(info.getValue())}</span>
      ),
    }),
    balanceHelper.accessor("credit", {
      header: t("accounting.columns.credit"),
      cell: (info) => (
        <span className="font-jetbrains">{formatAmount(info.getValue())}</span>
      ),
    }),
    balanceHelper.accessor("net", {
      header: t("accounting.columns.net"),
      cell: (info) => (
        <span
          className={
            Number(info.getValue()) < 0
              ? "font-jetbrains font-medium text-destructive"
              : "font-jetbrains font-medium"
          }
        >
          {formatAmount(info.getValue())}
        </span>
      ),
    }),
  ];
}
