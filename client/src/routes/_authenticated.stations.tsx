import { createFileRoute } from "@tanstack/react-router";
import { StationsPage } from "@/features/stations/components/stations-page";

export const Route = createFileRoute("/_authenticated/stations")({
  component: StationsPage,
});
