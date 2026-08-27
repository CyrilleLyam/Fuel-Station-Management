import { useSyncExternalStore } from "react";
import { X } from "lucide-react";
import { cn } from "@/lib/utils";
import { dismissToast, getToasts, subscribeToasts } from "@/lib/toast";

export function Toaster() {
  const toasts = useSyncExternalStore(subscribeToasts, getToasts, getToasts);

  if (toasts.length === 0) {
    return null;
  }

  return (
    <div className="fixed top-4 right-4 z-100 flex w-80 flex-col gap-2">
      {toasts.map((toast) => (
        <div
          key={toast.id}
          role="status"
          className={cn(
            "flex items-start justify-between gap-2 rounded-lg border px-3.5 py-3 text-sm shadow-lg",
            toast.variant === "error"
              ? "border-destructive/30 bg-destructive/10 text-destructive dark:border-destructive/40 dark:bg-destructive/15"
              : "border-border bg-popover text-popover-foreground",
          )}
        >
          <span>{toast.message}</span>
          <button
            type="button"
            onClick={() => dismissToast(toast.id)}
            aria-label="Dismiss"
            className="shrink-0 opacity-70 transition-opacity hover:opacity-100"
          >
            <X className="size-3.5" />
          </button>
        </div>
      ))}
    </div>
  );
}
