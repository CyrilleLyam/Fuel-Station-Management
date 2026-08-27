import { api } from "@/lib/axios";
import type { ApiResponse } from "@/lib/api-response";
import type {
  CreateUserValues,
  IamRole,
  IamUser,
  PermissionValues,
} from "../types/iam";

export async function listIamUsers(): Promise<IamUser[]> {
  const { data } = await api.get<ApiResponse<IamUser[]>>("/admin/iam/users");
  return data.data;
}

export async function listIamRoles(): Promise<IamRole[]> {
  const { data } = await api.get<ApiResponse<IamRole[]>>("/admin/iam/roles");
  return data.data;
}

export async function createIamUser(input: CreateUserValues): Promise<void> {
  await api.post("/admin/iam/users", input);
}

export async function assignRole(
  username: string,
  role: string,
): Promise<void> {
  await api.post("/admin/iam/roles/assign", { username, role });
}

export async function unassignRole(
  username: string,
  role: string,
): Promise<void> {
  await api.delete("/admin/iam/roles/assign", { data: { username, role } });
}

export async function grantPermission(
  role: string,
  permission: PermissionValues,
): Promise<void> {
  await api.post("/admin/iam/permissions", { role, ...permission });
}

export async function revokePermission(
  role: string,
  permission: PermissionValues,
): Promise<void> {
  await api.delete("/admin/iam/permissions", { data: { role, ...permission } });
}
