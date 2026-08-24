import { StatCard } from "./stat-card";
import type { DashboardStat } from "../types/dashboard";

const stats: DashboardStat[] = [
  { label: "Active pumps", value: "—", hint: "No pump feed connected" },
  { label: "Fuel in stock", value: "—", hint: "No tank feed connected" },
  { label: "Today's transactions", value: "—", hint: "No POS feed connected" },
  { label: "Open alerts", value: "—", hint: "No alert feed connected" },
];

export function DashboardPage() {
  return (
    <main className="flex flex-1 flex-col gap-6 p-6">
      <div className="flex flex-col gap-1">
        <h1 className="text-2xl font-semibold">Dashboard</h1>
        <p className="text-sm text-muted-foreground">
          Overview across your stations, pumps and tanks.
        </p>
      </div>
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-4">
        {stats.map((stat) => (
          <StatCard key={stat.label} {...stat} />
        ))}
      </div>
    </main>
  );
}
