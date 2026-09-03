export const ACCOUNT_CODES = [
  "CASH",
  "CARD_CLEARING",
  "MOBILE_MONEY",
  "ACCOUNTS_RECEIVABLE",
  "FUEL_SALES_REVENUE",
] as const;

export type AccountCode = (typeof ACCOUNT_CODES)[number];

export interface JournalLine {
  account: AccountCode;
  debit: number;
  credit: number;
}

export interface JournalEntry {
  id: number;
  reference: string;
  stationId: number;
  entryDate: string;
  memo: string | null;
  lines: JournalLine[];
  totalDebit: number;
  totalCredit: number;
  createdAt: string;
  updatedAt: string;
}

export interface AccountBalance {
  account: AccountCode;
  debit: number;
  credit: number;
  net: number;
}

export interface AccountingFilters {
  stationId?: number;
  from?: string;
  to?: string;
}
