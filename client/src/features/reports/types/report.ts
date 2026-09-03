export interface DailySalesRow {
  businessDate: string;
  quantity: number;
  totalAmount: number;
  transactions: number;
}

export interface ProductSalesRow {
  productId: number;
  quantity: number;
  totalAmount: number;
  transactions: number;
}

export interface AttendantSalesRow {
  attendant: string;
  quantity: number;
  totalAmount: number;
  transactions: number;
}

export interface ReportFilters {
  stationId?: number;
  from?: string;
  to?: string;
}
