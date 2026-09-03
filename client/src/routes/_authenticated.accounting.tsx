import { createFileRoute } from "@tanstack/react-router";
import { AccountingPage } from "@/features/accounting/components/accounting-page";

export const Route = createFileRoute("/_authenticated/accounting")({
  component: AccountingPage,
});
