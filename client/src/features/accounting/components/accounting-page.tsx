import { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { DataTable } from "@/components/data-table";
import {
  DateRangeFilter,
  type DateRange,
} from "@/components/date-range-filter";
import { PageHeader } from "@/components/page-header";
import { PaginationBar } from "@/components/pagination-bar";
import { TabNav } from "@/components/tab-nav";
import { Alert } from "@/components/ui/alert";
import { Select } from "@/components/ui/select";
import { getErrorMessage } from "@/features/auth/lib/get-error-message";
import { StatCard } from "@/features/dashboard/components/stat-card";
import { useStationOptions } from "@/features/stations/hooks/use-stations";
import { daysAgoIsoDate, formatAmount, toIsoDate } from "@/lib/format";
import {
  createJournalEntryColumns,
  createTrialBalanceColumns,
} from "./accounting-columns";
import {
  useJournalEntries,
  useTrialBalance,
} from "../hooks/use-accounting";

type AccountingTab = "entries" | "trialBalance";

const PAGE_SIZE = 10;

export function AccountingPage() {
  const { t } = useTranslation();
  const [tab, setTab] = useState<AccountingTab>("entries");
  const [page, setPage] = useState(0);
  const [stationFilter, setStationFilter] = useState("");
  const [range, setRange] = useState<DateRange>({
    from: daysAgoIsoDate(30),
    to: toIsoDate(new Date()),
  });

  const { data: stations } = useStationOptions();

  const filters = {
    stationId: stationFilter ? Number(stationFilter) : undefined,
    from: range.from || undefined,
    to: range.to || undefined,
  };

  const entries = useJournalEntries(
    { ...filters, page, size: PAGE_SIZE },
    tab === "entries",
  );
  const trialBalance = useTrialBalance(filters, tab === "trialBalance");

  const stationNames = useMemo(
    () => new Map(stations?.map((station) => [station.id, station.name])),
    [stations],
  );

  const balanceTotals = useMemo(() => {
    const rows = trialBalance.data ?? [];
    return rows.reduce(
      (totals, row) => ({
        debit: totals.debit + Number(row.debit),
        credit: totals.credit + Number(row.credit),
      }),
      { debit: 0, credit: 0 },
    );
  }, [trialBalance.data]);

  const active = tab === "entries" ? entries : trialBalance;

  const tabs: { id: AccountingTab; label: string }[] = [
    { id: "entries", label: t("accounting.tabs.entries") },
    { id: "trialBalance", label: t("accounting.tabs.trialBalance") },
  ];

  function updateFilters(next: () => void) {
    next();
    setPage(0);
  }

  return (
    <main className="flex flex-1 flex-col gap-6 p-6">
      <PageHeader
        title={t("accounting.title")}
        subtitle={t("accounting.subtitle")}
      />

      <div className="flex flex-wrap items-end gap-3">
        <div className="flex w-56 flex-col gap-1.5">
          <label className="font-jetbrains text-xs tracking-[0.15em] text-muted-foreground uppercase">
            {t("accounting.filters.station")}
          </label>
          <Select
            value={stationFilter}
            onChange={(event) =>
              updateFilters(() => setStationFilter(event.target.value))
            }
          >
            <option value="">{t("accounting.filters.allStations")}</option>
            {stations?.map((station) => (
              <option key={station.id} value={station.id}>
                {station.name}
              </option>
            ))}
          </Select>
        </div>
        <DateRangeFilter
          value={range}
          onChange={(next) => updateFilters(() => setRange(next))}
        />
      </div>

      <TabNav tabs={tabs} active={tab} onChange={setTab} />

      {active.isError && (
        <Alert>
          <span>{getErrorMessage(active.error)}</span>
        </Alert>
      )}

      {tab === "entries" && (
        <>
          <DataTable
            data={entries.data?.content ?? []}
            columns={createJournalEntryColumns(
              t,
              (id) => stationNames.get(id) ?? `#${id}`,
            )}
            isLoading={entries.isLoading}
            emptyMessage={t("accounting.noEntries")}
            getRowId={(row) => String(row.id)}
          />
          {entries.data?.meta && (
            <PaginationBar
              meta={entries.data.meta}
              isFetching={entries.isFetching}
              countLabel={t("accounting.entryCount", {
                count: entries.data.meta.totalElements,
              })}
              onPageChange={setPage}
            />
          )}
        </>
      )}

      {tab === "trialBalance" && (
        <>
          <div className="grid grid-cols-1 gap-4 sm:grid-cols-3">
            <StatCard
              label={t("accounting.totals.debit")}
              value={formatAmount(balanceTotals.debit)}
              hint={t("accounting.totals.debitHint")}
            />
            <StatCard
              label={t("accounting.totals.credit")}
              value={formatAmount(balanceTotals.credit)}
              hint={t("accounting.totals.creditHint")}
            />
            <StatCard
              label={t("accounting.totals.difference")}
              value={formatAmount(balanceTotals.debit - balanceTotals.credit)}
              hint={t("accounting.totals.differenceHint")}
            />
          </div>
          <DataTable
            data={trialBalance.data ?? []}
            columns={createTrialBalanceColumns(t)}
            isLoading={trialBalance.isLoading}
            emptyMessage={t("accounting.noBalances")}
            getRowId={(row) => row.account}
          />
        </>
      )}
    </main>
  );
}
