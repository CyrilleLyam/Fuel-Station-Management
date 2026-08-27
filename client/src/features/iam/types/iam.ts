import { z } from "zod";
import type { Permission } from "@/features/auth/types/auth";

export type { Permission };

export interface IamUser {
  id: string;
  username: string;
  email: string;
  enabled: boolean;
  roles: string[];
}

export interface IamRole {
  name: string;
  permissions: Permission[];
  users: string[];
}

export interface CreateUserValues {
  username: string;
  email: string;
  password: string;
}

export function createUserSchema(t: (key: string) => string) {
  return z.object({
    username: z.string().min(1, t("iam.users.form.usernameRequired")),
    email: z
      .string()
      .min(1, t("iam.users.form.emailRequired"))
      .pipe(z.email(t("iam.users.form.emailInvalid"))),
    password: z.string().min(8, t("iam.users.form.passwordMin")),
  });
}

export interface PermissionValues {
  resource: string;
  action: string;
}

export function createPermissionSchema(t: (key: string) => string) {
  return z.object({
    resource: z.string().min(1, t("iam.roles.form.resourceRequired")),
    action: z.string().min(1, t("iam.roles.form.actionRequired")),
  });
}

export function permissionKey(permission: Permission): string {
  return `${permission.resource}:${permission.action}`;
}
