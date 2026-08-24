import type { DashboardStat } from "../types/dashboard";

export function StatCard({ label, value, hint }: DashboardStat) {
  return (
    <div className="flex flex-col gap-1 rounded-lg border border-border bg-card p-5">
      <span className="font-jetbrains text-xs tracking-[0.15em] text-muted-foreground uppercase">
        {label}
      </span>
      <span className="text-2xl font-semibold">{value}</span>
      <span className="text-xs text-muted-foreground">{hint}</span>
    </div>
  );
}
