import type { ReactNode } from "react";
import { usePermissions } from "./use-permissions";

export function Can({
  resource,
  action,
  fallback = null,
  children,
}: {
  resource: string;
  action: string;
  fallback?: ReactNode;
  children: ReactNode;
}) {
  const { can } = usePermissions();
  return <>{can(resource, action) ? children : fallback}</>;
}
