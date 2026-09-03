import { createFileRoute } from "@tanstack/react-router";
import { SalesPage } from "@/features/sales/components/sales-page";

export const Route = createFileRoute("/_authenticated/sales")({
  component: SalesPage,
});
