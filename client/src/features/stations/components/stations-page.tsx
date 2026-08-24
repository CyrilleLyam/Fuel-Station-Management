import { useState } from "react";
import type { SortingState } from "@tanstack/react-table";
import { Plus, Search } from "lucide-react";
import { useTranslation } from "react-i18next";
import { Alert } from "@/components/ui/alert";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { getErrorMessage } from "@/features/auth/lib/get-error-message";
import { useDebouncedValue } from "@/lib/use-debounced-value";
import { useDeleteStation, useUpdateStation } from "../hooks/use-station-mutations";
import { useStations } from "../hooks/use-stations";
import { createStationColumns } from "./station-columns";
import { StationDialog } from "./station-dialog";
import { StationsTable } from "./stations-table";
import type { Station } from "../types/station";

const PAGE_SIZE = 10;

export function StationsPage() {
  const { t } = useTranslation();
  const [page, setPage] = useState(0);
  const [search, setSearch] = useState("");
  const [sorting, setSorting] = useState<SortingState>([]);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editingStation, setEditingStation] = useState<Station | undefined>();

  const keyword = useDebouncedValue(search.trim(), 300);

  const { data, isLoading, isError, error, isFetching } = useStations({
    keyword,
    page,
    size: PAGE_SIZE,
  });
  const updateStation = useUpdateStation();
  const deleteStation = useDeleteStation();

  function handleSearchChange(value: string) {
    setSearch(value);
    setPage(0);
  }

  function openCreate() {
    setEditingStation(undefined);
    setDialogOpen(true);
  }

  function openEdit(station: Station) {
    setEditingStation(station);
    setDialogOpen(true);
  }

  function toggleEnabled(station: Station) {
    updateStation.mutate({
      id: station.id,
      input: {
        name: station.name,
        code: station.code,
        address: station.address ?? "",
        phone: station.phone ?? "",
        latitude: station.latitude ?? undefined,
        longitude: station.longitude ?? undefined,
        enabled: !station.enabled,
      },
    });
  }

  function remove(station: Station) {
    if (confirm(t("stations.deleteConfirm", { name: station.name }))) {
      deleteStation.mutate(station.id);
    }
  }

  const columns = createStationColumns(t, {
    onEdit: openEdit,
    onToggleEnabled: toggleEnabled,
    onDelete: remove,
  });

  const meta = data?.meta;
  const canPrev = page > 0;
  const canNext = meta ? page + 1 < meta.totalPages : false;

  return (
    <main className="flex flex-1 flex-col gap-6 p-6">
      <div className="flex items-start justify-between gap-4">
        <div className="flex flex-col gap-1">
          <h1 className="text-2xl font-semibold">{t("stations.title")}</h1>
          <p className="text-sm text-muted-foreground">
            {t("stations.subtitle")}
          </p>
        </div>
        <Button onClick={openCreate} className="gap-1.5">
          <Plus className="size-4" />
          {t("stations.addStation")}
        </Button>
      </div>

      <div className="relative max-w-xs">
        <Search className="pointer-events-none absolute top-1/2 left-2.5 size-3.5 -translate-y-1/2 text-muted-foreground" />
        <Input
          value={search}
          onChange={(event) => handleSearchChange(event.target.value)}
          placeholder={t("stations.searchPlaceholder")}
          className="pl-8"
        />
      </div>

      {isError && (
        <Alert>
          <span>{getErrorMessage(error)}</span>
        </Alert>
      )}

      <StationsTable
        data={data?.content ?? []}
        columns={columns}
        isLoading={isLoading}
        sorting={sorting}
        onSortingChange={setSorting}
      />

      {meta && (
        <div className="flex items-center justify-between text-sm text-muted-foreground">
          <span>
            {t("stations.pageInfo", {
              page: meta.page + 1,
              totalPages: Math.max(meta.totalPages, 1),
            })}{" "}
            · {t("stations.stationCount", { count: meta.totalElements })}
            {isFetching && ` · ${t("stations.refreshing")}`}
          </span>
          <div className="flex gap-2">
            <Button
              variant="outline"
              size="sm"
              disabled={!canPrev}
              onClick={() => setPage((p) => Math.max(p - 1, 0))}
            >
              {t("stations.previous")}
            </Button>
            <Button
              variant="outline"
              size="sm"
              disabled={!canNext}
              onClick={() => setPage((p) => p + 1)}
            >
              {t("stations.next")}
            </Button>
          </div>
        </div>
      )}

      <StationDialog
        station={editingStation}
        open={dialogOpen}
        onOpenChange={setDialogOpen}
      />
    </main>
  );
}
