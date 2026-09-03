import { useMemo, useState } from "react";
import type { SortingState } from "@tanstack/react-table";
import { Plus, Search } from "lucide-react";
import { useTranslation } from "react-i18next";
import { DataTable } from "@/components/data-table";
import { PageHeader } from "@/components/page-header";
import { PaginationBar } from "@/components/pagination-bar";
import { Alert } from "@/components/ui/alert";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Select } from "@/components/ui/select";
import { getErrorMessage } from "@/features/auth/lib/get-error-message";
import { usePermissions } from "@/features/iam/permissions/use-permissions";
import { useActiveProducts } from "@/features/products/hooks/use-products";
import { useStationOptions } from "@/features/stations/hooks/use-stations";
import { useDebouncedValue } from "@/lib/use-debounced-value";
import { createTankColumns } from "./tank-columns";
import { TankDialog } from "./tank-dialog";
import { TankMovementDialog } from "./tank-movement-dialog";
import { useDeleteTank, useUpdateTank } from "../hooks/use-tank-mutations";
import { useTanks } from "../hooks/use-tanks";
import type { Tank, TankMovementKind } from "../types/tank";

const PAGE_SIZE = 10;

export function TanksPage() {
  const { t } = useTranslation();
  const { can } = usePermissions();
  const canCreate = can("tank", "create");
  const canUpdate = can("tank", "update");
  const canDelete = can("tank", "delete");
  const [page, setPage] = useState(0);
  const [search, setSearch] = useState("");
  const [stationFilter, setStationFilter] = useState("");
  const [sorting, setSorting] = useState<SortingState>([]);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [movementOpen, setMovementOpen] = useState(false);
  const [movementKind, setMovementKind] =
    useState<TankMovementKind>("delivery");
  const [selected, setSelected] = useState<Tank | undefined>();

  const keyword = useDebouncedValue(search.trim(), 300);
  const { data: stations } = useStationOptions();
  const { data: products } = useActiveProducts();

  const { data, isLoading, isError, error, isFetching } = useTanks({
    stationId: stationFilter ? Number(stationFilter) : undefined,
    keyword,
    page,
    size: PAGE_SIZE,
  });
  const updateTank = useUpdateTank();
  const deleteTank = useDeleteTank();

  const stationNames = useMemo(
    () => new Map(stations?.map((station) => [station.id, station.name])),
    [stations],
  );
  const productNames = useMemo(
    () => new Map(products?.map((product) => [product.id, product.name])),
    [products],
  );

  function openCreate() {
    setSelected(undefined);
    setDialogOpen(true);
  }

  function openEdit(tank: Tank) {
    setSelected(tank);
    setDialogOpen(true);
  }

  function openMovement(tank: Tank, kind: TankMovementKind) {
    setSelected(tank);
    setMovementKind(kind);
    setMovementOpen(true);
  }

  function toggleActive(tank: Tank) {
    updateTank.mutate({
      id: tank.id,
      input: { label: tank.label, active: !tank.active },
    });
  }

  function remove(tank: Tank) {
    if (confirm(t("tanks.deleteConfirm", { label: tank.label }))) {
      deleteTank.mutate(tank.id);
    }
  }

  const columns = createTankColumns(
    t,
    {
      stationName: (id) => stationNames.get(id) ?? `#${id}`,
      productName: (id) =>
        id == null ? t("tanks.unassigned") : (productNames.get(id) ?? `#${id}`),
    },
    {
      onEdit: openEdit,
      onDelivery: (tank) => openMovement(tank, "delivery"),
      onDispense: (tank) => openMovement(tank, "dispense"),
      onToggleActive: toggleActive,
      onDelete: remove,
      canUpdate,
      canDelete,
    },
  );

  return (
    <main className="flex flex-1 flex-col gap-6 p-6">
      <PageHeader
        title={t("tanks.title")}
        subtitle={t("tanks.subtitle")}
        actions={
          canCreate && (
            <Button onClick={openCreate} className="gap-1.5">
              <Plus className="size-4" />
              {t("tanks.addTank")}
            </Button>
          )
        }
      />

      <div className="flex flex-wrap items-center gap-3">
        <div className="relative max-w-xs flex-1">
          <Search className="pointer-events-none absolute top-1/2 left-2.5 size-3.5 -translate-y-1/2 text-muted-foreground" />
          <Input
            value={search}
            onChange={(event) => {
              setSearch(event.target.value);
              setPage(0);
            }}
            placeholder={t("tanks.searchPlaceholder")}
            className="pl-8"
          />
        </div>
        <div className="w-56">
          <Select
            value={stationFilter}
            onChange={(event) => {
              setStationFilter(event.target.value);
              setPage(0);
            }}
          >
            <option value="">{t("tanks.allStations")}</option>
            {stations?.map((station) => (
              <option key={station.id} value={station.id}>
                {station.name}
              </option>
            ))}
          </Select>
        </div>
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
        emptyMessage={t("tanks.noResults")}
        sorting={sorting}
        onSortingChange={setSorting}
        getRowId={(row) => String(row.id)}
      />

      {data?.meta && (
        <PaginationBar
          meta={data.meta}
          isFetching={isFetching}
          countLabel={t("tanks.tankCount", { count: data.meta.totalElements })}
          onPageChange={setPage}
        />
      )}

      <TankDialog
        tank={selected}
        open={dialogOpen}
        onOpenChange={setDialogOpen}
      />
      <TankMovementDialog
        tank={selected}
        kind={movementKind}
        open={movementOpen}
        onOpenChange={setMovementOpen}
      />
    </main>
  );
}
