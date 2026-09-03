import { keepPreviousData, useQuery } from "@tanstack/react-query";
import {
  getTrialBalance,
  listJournalEntries,
  type ListJournalEntriesParams,
} from "../api/accounting.api";
import type { AccountingFilters } from "../types/accounting";

export function useJournalEntries(
  params: ListJournalEntriesParams,
  enabled = true,
) {
  return useQuery({
    queryKey: ["accounting", "journal-entries", params],
    queryFn: () => listJournalEntries(params),
    placeholderData: keepPreviousData,
    enabled,
  });
}

export function useTrialBalance(filters: AccountingFilters, enabled = true) {
  return useQuery({
    queryKey: ["accounting", "trial-balance", filters],
    queryFn: () => getTrialBalance(filters),
    placeholderData: keepPreviousData,
    enabled,
  });
}
