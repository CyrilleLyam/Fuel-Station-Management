import { useMutation, useQueryClient } from "@tanstack/react-query";
import {
  assignRole,
  createIamUser,
  grantPermission,
  revokePermission,
  unassignRole,
} from "../api/iam.api";
import type { PermissionValues } from "../types/iam";

function useIamInvalidator() {
  const queryClient = useQueryClient();
  return () => {
    queryClient.invalidateQueries({ queryKey: ["iam"] });
    queryClient.invalidateQueries({ queryKey: ["users", "me"] });
  };
}

export function useCreateIamUser() {
  const invalidate = useIamInvalidator();
  return useMutation({
    mutationFn: createIamUser,
    onSuccess: invalidate,
  });
}

interface RoleAssignmentVars {
  username: string;
  role: string;
}

export function useAssignRole() {
  const invalidate = useIamInvalidator();
  return useMutation({
    mutationFn: ({ username, role }: RoleAssignmentVars) =>
      assignRole(username, role),
    onSuccess: invalidate,
  });
}

export function useUnassignRole() {
  const invalidate = useIamInvalidator();
  return useMutation({
    mutationFn: ({ username, role }: RoleAssignmentVars) =>
      unassignRole(username, role),
    onSuccess: invalidate,
  });
}

interface PermissionVars {
  role: string;
  permission: PermissionValues;
}

export function useGrantPermission() {
  const invalidate = useIamInvalidator();
  return useMutation({
    mutationFn: ({ role, permission }: PermissionVars) =>
      grantPermission(role, permission),
    onSuccess: invalidate,
  });
}

export function useRevokePermission() {
  const invalidate = useIamInvalidator();
  return useMutation({
    mutationFn: ({ role, permission }: PermissionVars) =>
      revokePermission(role, permission),
    onSuccess: invalidate,
  });
}
