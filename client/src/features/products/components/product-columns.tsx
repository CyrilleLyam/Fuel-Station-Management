import { createColumnHelper, type ColumnDef } from "@tanstack/react-table";
import { Pencil, Tag, Trash2 } from "lucide-react";
import type { TFunction } from "i18next";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { formatAmount } from "@/lib/format";
import type { Product } from "../types/product";

const columnHelper = createColumnHelper<Product>();

export function createProductColumns(
  t: TFunction,
  actions: {
    onEdit: (product: Product) => void;
    onChangePrice: (product: Product) => void;
    onToggleActive: (product: Product) => void;
    onDelete: (product: Product) => void;
    canUpdate: boolean;
    canDelete: boolean;
  },
): ColumnDef<Product, any>[] {
  return [
    columnHelper.accessor("name", {
      header: t("products.columns.name"),
      cell: (info) => <span className="font-medium">{info.getValue()}</span>,
    }),
    columnHelper.accessor("sku", {
      header: t("products.columns.sku"),
      cell: (info) => (
        <span className="font-jetbrains text-muted-foreground">
          {info.getValue()}
        </span>
      ),
    }),
    columnHelper.accessor("fuelType", {
      header: t("products.columns.fuelType"),
      cell: (info) => (
        <Badge variant="outline">
          {t(`products.fuelTypes.${info.getValue()}`)}
        </Badge>
      ),
    }),
    columnHelper.accessor("unit", {
      header: t("products.columns.unit"),
      cell: (info) => (
        <span className="text-muted-foreground">{info.getValue()}</span>
      ),
    }),
    columnHelper.accessor("unitPrice", {
      header: t("products.columns.unitPrice"),
      cell: (info) => (
        <span className="font-jetbrains">{formatAmount(info.getValue())}</span>
      ),
    }),
    columnHelper.accessor("active", {
      header: t("products.columns.status"),
      cell: (info) => {
        const product = info.row.original;
        return (
          <button
            type="button"
            disabled={!actions.canUpdate}
            onClick={() => actions.canUpdate && actions.onToggleActive(product)}
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
              ? t("products.status.active")
              : t("products.status.inactive")}
          </button>
        );
      },
    }),
    columnHelper.display({
      id: "actions",
      header: "",
      cell: (info) => {
        const product = info.row.original;
        return (
          <div className="flex justify-end gap-1">
            {actions.canUpdate && (
              <Button
                variant="ghost"
                size="icon-sm"
                onClick={() => actions.onChangePrice(product)}
                aria-label={t("products.actions.changePrice")}
              >
                <Tag className="size-3.5" />
              </Button>
            )}
            {actions.canUpdate && (
              <Button
                variant="ghost"
                size="icon-sm"
                onClick={() => actions.onEdit(product)}
                aria-label={t("products.actions.edit")}
              >
                <Pencil className="size-3.5" />
              </Button>
            )}
            {actions.canDelete && (
              <Button
                variant="ghost"
                size="icon-sm"
                onClick={() => actions.onDelete(product)}
                aria-label={t("products.actions.delete")}
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
