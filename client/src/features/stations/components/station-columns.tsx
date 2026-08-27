import { createColumnHelper, type ColumnDef } from "@tanstack/react-table";
import { Pencil, Trash2 } from "lucide-react";
import type { TFunction } from "i18next";
import { Button } from "@/components/ui/button";
import type { Station } from "../types/station";

const columnHelper = createColumnHelper<Station>();

export function createStationColumns(
  t: TFunction,
  actions: {
    onEdit: (station: Station) => void;
    onToggleEnabled: (station: Station) => void;
    onDelete: (station: Station) => void;
    canUpdate: boolean;
    canDelete: boolean;
  },
): ColumnDef<Station, any>[] {
  return [
    columnHelper.accessor("name", {
      header: t("stations.columns.name"),
      cell: (info) => (
        <span className="font-medium">{info.getValue()}</span>
      ),
    }),
    columnHelper.accessor("code", {
      header: t("stations.columns.code"),
      cell: (info) => (
        <span className="font-jetbrains text-muted-foreground">
          {info.getValue()}
        </span>
      ),
    }),
    columnHelper.accessor("address", {
      header: t("stations.columns.address"),
      cell: (info) => (
        <span className="text-muted-foreground">
          {info.getValue() || "—"}
        </span>
      ),
    }),
    columnHelper.accessor("phone", {
      header: t("stations.columns.phone"),
      cell: (info) => (
        <span className="text-muted-foreground">
          {info.getValue() || "—"}
        </span>
      ),
    }),
    columnHelper.accessor("enabled", {
      header: t("stations.columns.status"),
      cell: (info) => {
        const station = info.row.original;
        return (
          <button
            type="button"
            disabled={!actions.canUpdate}
            onClick={() =>
              actions.canUpdate && actions.onToggleEnabled(station)
            }
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
              ? t("stations.status.enabled")
              : t("stations.status.disabled")}
          </button>
        );
      },
    }),
    columnHelper.display({
      id: "actions",
      header: "",
      cell: (info) => {
        const station = info.row.original;
        return (
          <div className="flex justify-end gap-1">
            {actions.canUpdate && (
              <Button
                variant="ghost"
                size="icon-sm"
                onClick={() => actions.onEdit(station)}
                aria-label={t("stations.actions.edit")}
              >
                <Pencil className="size-3.5" />
              </Button>
            )}
            {actions.canDelete && (
              <Button
                variant="ghost"
                size="icon-sm"
                onClick={() => actions.onDelete(station)}
                aria-label={t("stations.actions.delete")}
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
