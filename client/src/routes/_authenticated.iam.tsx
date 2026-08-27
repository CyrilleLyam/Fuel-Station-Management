import { createFileRoute } from "@tanstack/react-router";
import { IamPage } from "@/features/iam/components/iam-page";

export const Route = createFileRoute("/_authenticated/iam")({
  component: IamPage,
});
