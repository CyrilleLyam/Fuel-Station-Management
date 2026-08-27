import { useState } from "react";
import { Loader2, Users, X } from "lucide-react";
import { useTranslation } from "react-i18next";
import { Alert } from "@/components/ui/alert";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { getErrorMessage } from "@/features/auth/lib/get-error-message";
import { showToast } from "@/lib/toast";
import {
  useGrantPermission,
  useRevokePermission,
} from "../hooks/use-iam-mutations";
import { useIamRoles } from "../hooks/use-iam";
import { permissionKey, type IamRole } from "../types/iam";

export function RolesPanel() {
  const { t } = useTranslation();
  const { data: roles, isLoading, isError, error } = useIamRoles();

  return (
    <div className="flex flex-col gap-4">
      <p className="text-sm text-muted-foreground">{t("iam.roles.hint")}</p>

      {isError && (
        <Alert>
          <span>{getErrorMessage(error)}</span>
        </Alert>
      )}

      {isLoading && (
        <Loader2 className="size-5 animate-spin text-muted-foreground" />
      )}

      {!isLoading && (roles?.length ?? 0) === 0 && (
        <div className="rounded-xl border border-border px-4 py-10 text-center text-sm text-muted-foreground">
          {t("iam.roles.empty")}
        </div>
      )}

      <div className="grid grid-cols-1 gap-4 lg:grid-cols-2">
        {roles?.map((role) => <RoleCard key={role.name} role={role} />)}
      </div>
    </div>
  );
}

function RoleCard({ role }: { role: IamRole }) {
  const { t } = useTranslation();
  const grant = useGrantPermission();
  const revoke = useRevokePermission();
  const [resource, setResource] = useState("");
  const [action, setAction] = useState("");

  const pending = grant.isPending || revoke.isPending;

  function addPermission() {
    const nextResource = resource.trim().toLowerCase();
    const nextAction = action.trim().toLowerCase();
    if (!nextResource || !nextAction) {
      return;
    }
    grant.mutate(
      { role: role.name, permission: { resource: nextResource, action: nextAction } },
      {
        onSuccess: () => {
          showToast(t("iam.roles.permissionGranted"), "success");
          setResource("");
          setAction("");
        },
      },
    );
  }

  return (
    <div className="flex flex-col gap-3 rounded-xl border border-border p-4">
      <div className="flex items-center justify-between gap-2">
        <span className="font-display font-semibold">{role.name}</span>
        <span className="inline-flex items-center gap-1 text-xs text-muted-foreground">
          <Users className="size-3.5" />
          {t("iam.roles.memberCount", { count: role.users.length })}
        </span>
      </div>

      <div className="flex flex-wrap gap-1.5">
        {role.permissions.length === 0 && (
          <span className="text-sm text-muted-foreground">
            {t("iam.roles.noPermissions")}
          </span>
        )}
        {role.permissions.map((permission) => (
          <Badge key={permissionKey(permission)} className="pr-1 font-jetbrains">
            {permissionKey(permission)}
            <button
              type="button"
              onClick={() =>
                revoke.mutate(
                  { role: role.name, permission },
                  {
                    onSuccess: () =>
                      showToast(t("iam.roles.permissionRevoked"), "success"),
                  },
                )
              }
              disabled={pending}
              aria-label={t("iam.roles.revoke", {
                permission: permissionKey(permission),
              })}
              className="rounded-full p-0.5 hover:bg-foreground/10 disabled:opacity-50"
            >
              <X className="size-3" />
            </button>
          </Badge>
        ))}
      </div>

      <div className="flex gap-2">
        <Input
          value={resource}
          onChange={(event) => setResource(event.target.value)}
          placeholder={t("iam.roles.resourcePlaceholder")}
          className="h-7 text-xs"
        />
        <Input
          value={action}
          onChange={(event) => setAction(event.target.value)}
          placeholder={t("iam.roles.actionPlaceholder")}
          className="h-7 text-xs"
          onKeyDown={(event) => {
            if (event.key === "Enter") {
              event.preventDefault();
              addPermission();
            }
          }}
        />
        <Button
          type="button"
          size="sm"
          onClick={addPermission}
          disabled={pending || !resource.trim() || !action.trim()}
          className="shrink-0"
        >
          {t("iam.roles.grant")}
        </Button>
      </div>

      {(grant.error || revoke.error) && (
        <p className="text-xs text-destructive">
          {getErrorMessage(grant.error ?? revoke.error)}
        </p>
      )}
    </div>
  );
}
