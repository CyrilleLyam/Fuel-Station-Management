import type { ComponentProps } from "react"

import { cn } from "@/lib/utils"

function Alert({ className, ...props }: ComponentProps<"div">) {
  return (
    <div
      role="alert"
      data-slot="alert"
      className={cn(
        "flex items-start gap-2.5 rounded-lg border border-destructive/30 bg-destructive/10 px-3.5 py-3 text-sm text-destructive dark:border-destructive/40 dark:bg-destructive/15",
        className,
      )}
      {...props}
    />
  )
}

export { Alert }
