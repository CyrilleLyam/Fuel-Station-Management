import { useState } from "react";
import { ShieldAlert } from "lucide-react";
import { useTranslation } from "react-i18next";
import { TabNav } from "@/components/tab-nav";
import { usePermissions } from "../permissions/use-permissions";
import { RolesPanel } from "./roles-panel";
import { UsersPanel } from "./users-panel";

type Tab = "users" | "roles";

export function IamPage() {
  const { t } = useTranslation();
  const { can, isLoading } = usePermissions();
  const [tab, setTab] = useState<Tab>("users");

  if (isLoading) {
    return null;
  }

  if (!can("iam", "admin")) {
    return (
      <main className="flex flex-1 flex-col items-center justify-center gap-3 p-6 text-center">
        <ShieldAlert className="size-8 text-muted-foreground" />
        <h1 className="text-lg font-semibold">{t("iam.denied.title")}</h1>
        <p className="max-w-sm text-sm text-muted-foreground">
          {t("iam.denied.subtitle")}
        </p>
      </main>
    );
  }

  const tabs: { id: Tab; label: string }[] = [
    { id: "users", label: t("iam.tabs.users") },
    { id: "roles", label: t("iam.tabs.roles") },
  ];

  return (
    <main className="flex flex-1 flex-col gap-6 p-6">
      <div className="flex flex-col gap-1">
        <h1 className="text-2xl font-semibold">{t("iam.title")}</h1>
        <p className="text-sm text-muted-foreground">{t("iam.subtitle")}</p>
      </div>

      <TabNav tabs={tabs} active={tab} onChange={setTab} />

      {tab === "users" ? <UsersPanel /> : <RolesPanel />}
    </main>
  );
}
