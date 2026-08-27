import { useCurrentUser } from "@/features/auth/hooks/use-current-user";

export function usePermissions() {
  const { data, isLoading, isError } = useCurrentUser();

  const permissions = data?.permissions ?? [];
  const roles = data?.roles ?? [];

  function can(resource: string, action: string): boolean {
    return permissions.some(
      (permission) =>
        permission.resource === resource && permission.action === action,
    );
  }

  return { can, roles, permissions, isLoading, isError };
}
