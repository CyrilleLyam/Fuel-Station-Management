import { api } from "@/lib/axios";
import type { ApiResponse, PageMeta } from "@/lib/api-response";
import type {
  AccountBalance,
  AccountingFilters,
  JournalEntry,
} from "../types/accounting";

export interface JournalEntryPage {
  content: JournalEntry[];
  meta: PageMeta;
}

export interface ListJournalEntriesParams extends AccountingFilters {
  page?: number;
  size?: number;
}

export async function listJournalEntries(
  params: ListJournalEntriesParams = {},
): Promise<JournalEntryPage> {
  const { data } = await api.get<ApiResponse<JournalEntry[]>>(
    "/accounting/journal-entries",
    {
      params: {
        stationId: params.stationId ?? undefined,
        from: params.from || undefined,
        to: params.to || undefined,
        page: params.page ?? 0,
        size: params.size ?? 20,
      },
    },
  );
  return { content: data.data, meta: data.meta! };
}

export async function getTrialBalance(
  filters: AccountingFilters,
): Promise<AccountBalance[]> {
  const { data } = await api.get<ApiResponse<AccountBalance[]>>(
    "/accounting/trial-balance",
    {
      params: {
        stationId: filters.stationId ?? undefined,
        from: filters.from || undefined,
        to: filters.to || undefined,
      },
    },
  );
  return data.data;
}
