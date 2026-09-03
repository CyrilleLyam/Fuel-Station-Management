import { createFileRoute } from "@tanstack/react-router";
import { TanksPage } from "@/features/tanks/components/tanks-page";

export const Route = createFileRoute("/_authenticated/tanks")({
  component: TanksPage,
});
