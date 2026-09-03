import type { ReactNode } from "react";
import { cn } from "@/lib/utils";

export function FormField({
  label,
  error,
  span,
  children,
}: {
  label: string;
  error?: string;
  span?: boolean;
  children: ReactNode;
}) {
  return (
    <div className={cn("flex flex-col gap-1.5", span && "sm:col-span-2")}>
      <label className="font-jetbrains text-xs tracking-[0.15em] text-muted-foreground uppercase">
        {label}
      </label>
      {children}
      {error && <p className="text-sm text-destructive">{error}</p>}
    </div>
  );
}
