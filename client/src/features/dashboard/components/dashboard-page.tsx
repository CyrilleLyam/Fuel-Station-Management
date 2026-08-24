import { useTranslation } from "react-i18next";
import { StatCard } from "./stat-card";

const STAT_KEYS = [
  "activePumps",
  "fuelInStock",
  "todaysTransactions",
  "openAlerts",
] as const;

export function DashboardPage() {
  const { t } = useTranslation();

  return (
    <main className="flex flex-1 flex-col gap-6 p-6">
      <div className="flex flex-col gap-1">
        <h1 className="text-2xl font-semibold">{t("dashboard.title")}</h1>
        <p className="text-sm text-muted-foreground">
          {t("dashboard.subtitle")}
        </p>
      </div>
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-4">
        {STAT_KEYS.map((key) => (
          <StatCard
            key={key}
            label={t(`dashboard.stats.${key}.label`)}
            value="—"
            hint={t(`dashboard.stats.${key}.hint`)}
          />
        ))}
      </div>
    </main>
  );
}
