import { useState } from "react";
import type { SortingState } from "@tanstack/react-table";
import { Plus, Search } from "lucide-react";
import { useTranslation } from "react-i18next";
import { DataTable } from "@/components/data-table";
import { PageHeader } from "@/components/page-header";
import { PaginationBar } from "@/components/pagination-bar";
import { Alert } from "@/components/ui/alert";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { getErrorMessage } from "@/features/auth/lib/get-error-message";
import { usePermissions } from "@/features/iam/permissions/use-permissions";
import { useDebouncedValue } from "@/lib/use-debounced-value";
import { createProductColumns } from "./product-columns";
import { ProductDialog } from "./product-dialog";
import { ProductPriceDialog } from "./product-price-dialog";
import {
  useDeleteProduct,
  useUpdateProduct,
} from "../hooks/use-product-mutations";
import { useProducts } from "../hooks/use-products";
import type { Product } from "../types/product";

const PAGE_SIZE = 10;

export function ProductsPage() {
  const { t } = useTranslation();
  const { can } = usePermissions();
  const canCreate = can("product", "create");
  const canUpdate = can("product", "update");
  const canDelete = can("product", "delete");
  const [page, setPage] = useState(0);
  const [search, setSearch] = useState("");
  const [sorting, setSorting] = useState<SortingState>([]);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [priceOpen, setPriceOpen] = useState(false);
  const [selected, setSelected] = useState<Product | undefined>();

  const keyword = useDebouncedValue(search.trim(), 300);

  const { data, isLoading, isError, error, isFetching } = useProducts({
    keyword,
    page,
    size: PAGE_SIZE,
  });
  const updateProduct = useUpdateProduct();
  const deleteProduct = useDeleteProduct();

  function handleSearchChange(value: string) {
    setSearch(value);
    setPage(0);
  }

  function openCreate() {
    setSelected(undefined);
    setDialogOpen(true);
  }

  function openEdit(product: Product) {
    setSelected(product);
    setDialogOpen(true);
  }

  function openPrice(product: Product) {
    setSelected(product);
    setPriceOpen(true);
  }

  function toggleActive(product: Product) {
    updateProduct.mutate({
      id: product.id,
      input: {
        name: product.name,
        fuelType: product.fuelType,
        unit: product.unit,
        active: !product.active,
      },
    });
  }

  function remove(product: Product) {
    if (confirm(t("products.deleteConfirm", { name: product.name }))) {
      deleteProduct.mutate(product.id);
    }
  }

  const columns = createProductColumns(t, {
    onEdit: openEdit,
    onChangePrice: openPrice,
    onToggleActive: toggleActive,
    onDelete: remove,
    canUpdate,
    canDelete,
  });

  return (
    <main className="flex flex-1 flex-col gap-6 p-6">
      <PageHeader
        title={t("products.title")}
        subtitle={t("products.subtitle")}
        actions={
          canCreate && (
            <Button onClick={openCreate} className="gap-1.5">
              <Plus className="size-4" />
              {t("products.addProduct")}
            </Button>
          )
        }
      />

      <div className="relative max-w-xs">
        <Search className="pointer-events-none absolute top-1/2 left-2.5 size-3.5 -translate-y-1/2 text-muted-foreground" />
        <Input
          value={search}
          onChange={(event) => handleSearchChange(event.target.value)}
          placeholder={t("products.searchPlaceholder")}
          className="pl-8"
        />
      </div>

      {isError && (
        <Alert>
          <span>{getErrorMessage(error)}</span>
        </Alert>
      )}

      <DataTable
        data={data?.content ?? []}
        columns={columns}
        isLoading={isLoading}
        emptyMessage={t("products.noResults")}
        sorting={sorting}
        onSortingChange={setSorting}
        getRowId={(row) => String(row.id)}
      />

      {data?.meta && (
        <PaginationBar
          meta={data.meta}
          isFetching={isFetching}
          countLabel={t("products.productCount", {
            count: data.meta.totalElements,
          })}
          onPageChange={setPage}
        />
      )}

      <ProductDialog
        product={selected}
        open={dialogOpen}
        onOpenChange={setDialogOpen}
      />
      <ProductPriceDialog
        product={selected}
        open={priceOpen}
        onOpenChange={setPriceOpen}
      />
    </main>
  );
}
