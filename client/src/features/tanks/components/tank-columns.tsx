import { createColumnHelper, type ColumnDef } from "@tanstack/react-table";
import { ArrowDownToLine, ArrowUpFromLine, Pencil, Trash2 } from "lucide-react";
import type { TFunction } from "i18next";
import { Button } from "@/components/ui/button";
import { formatQuantity } from "@/lib/format";
import type { Tank } from "../types/tank";

const columnHelper = createColumnHelper<Tank>();

export function createTankColumns(
  t: TFunction,
  lookups: {
    stationName: (id: number) => string;
    productName: (id: number | null) => string;
  },
  actions: {
    onEdit: (tank: Tank) => void;
    onDelivery: (tank: Tank) => void;
    onDispense: (tank: Tank) => void;
    onToggleActive: (tank: Tank) => void;
    onDelete: (tank: Tank) => void;
    canUpdate: boolean;
    canDelete: boolean;
  },
): ColumnDef<Tank, any>[] {
  return [
    columnHelper.accessor("label", {
      header: t("tanks.columns.label"),
      cell: (info) => <span className="font-medium">{info.getValue()}</span>,
    }),
    columnHelper.accessor("stationId", {
      header: t("tanks.columns.station"),
      cell: (info) => (
        <span className="text-muted-foreground">
          {lookups.stationName(info.getValue())}
        </span>
      ),
    }),
    columnHelper.accessor("productId", {
      header: t("tanks.columns.product"),
      cell: (info) => (
        <span className="text-muted-foreground">
          {lookups.productName(info.getValue())}
        </span>
      ),
    }),
    columnHelper.accessor("currentQuantity", {
      header: t("tanks.columns.level"),
      cell: (info) => {
        const tank = info.row.original;
        const capacity = Number(tank.capacity);
        const current = Number(tank.currentQuantity);
        const ratio = capacity > 0 ? Math.min(current / capacity, 1) : 0;
        return (
          <div className="flex min-w-40 flex-col gap-1">
            <span className="font-jetbrains text-xs">
              {formatQuantity(current)} / {formatQuantity(capacity)}
            </span>
            <div className="h-1.5 w-full overflow-hidden rounded-full bg-muted">
              <div
                className={
                  ratio < 0.15
                    ? "h-full rounded-full bg-destructive"
                    : ratio < 0.35
                      ? "h-full rounded-full bg-amber-500"
                      : "h-full rounded-full bg-primary"
                }
                style={{ width: `${ratio * 100}%` }}
              />
            </div>
          </div>
        );
      },
    }),
    columnHelper.accessor("availableSpace", {
      header: t("tanks.columns.availableSpace"),
      cell: (info) => (
        <span className="font-jetbrains text-muted-foreground">
          {formatQuantity(info.getValue())}
        </span>
      ),
    }),
    columnHelper.accessor("active", {
      header: t("tanks.columns.status"),
      cell: (info) => {
        const tank = info.row.original;
        return (
          <button
            type="button"
            disabled={!actions.canUpdate}
            onClick={() => actions.canUpdate && actions.onToggleActive(tank)}
            className={
              info.getValue()
                ? "inline-flex items-center gap-1.5 rounded-full bg-green-50 px-2.5 py-1 text-xs font-medium text-green-700 transition-colors hover:bg-green-100 dark:bg-green-500/10 dark:text-green-400 dark:hover:bg-green-500/15"
                : "inline-flex items-center gap-1.5 rounded-full bg-muted px-2.5 py-1 text-xs font-medium text-muted-foreground transition-colors hover:bg-muted/70"
            }
          >
            <span
              className={
                info.getValue()
                  ? "size-1.5 rounded-full bg-green-600 dark:bg-green-400"
                  : "size-1.5 rounded-full bg-muted-foreground/50"
              }
            />
            {info.getValue()
              ? t("tanks.status.active")
              : t("tanks.status.inactive")}
          </button>
        );
      },
    }),
    columnHelper.display({
      id: "actions",
      header: "",
      cell: (info) => {
        const tank = info.row.original;
        return (
          <div className="flex justify-end gap-1">
            {actions.canUpdate && (
              <Button
                variant="ghost"
                size="icon-sm"
                onClick={() => actions.onDelivery(tank)}
                aria-label={t("tanks.actions.delivery")}
              >
                <ArrowDownToLine className="size-3.5" />
              </Button>
            )}
            {actions.canUpdate && (
              <Button
                variant="ghost"
                size="icon-sm"
                onClick={() => actions.onDispense(tank)}
                aria-label={t("tanks.actions.dispense")}
              >
                <ArrowUpFromLine className="size-3.5" />
              </Button>
            )}
            {actions.canUpdate && (
              <Button
                variant="ghost"
                size="icon-sm"
                onClick={() => actions.onEdit(tank)}
                aria-label={t("tanks.actions.edit")}
              >
                <Pencil className="size-3.5" />
              </Button>
            )}
            {actions.canDelete && (
              <Button
                variant="ghost"
                size="icon-sm"
                onClick={() => actions.onDelete(tank)}
                aria-label={t("tanks.actions.delete")}
              >
                <Trash2 className="size-3.5" />
              </Button>
            )}
          </div>
        );
      },
    }),
  ];
}
