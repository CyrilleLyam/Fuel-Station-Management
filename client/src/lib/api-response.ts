export interface PageMeta {
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface ApiResponse<T> {
  error: boolean;
  message: string;
  data: T;
  meta?: PageMeta;
}
