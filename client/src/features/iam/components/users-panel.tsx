import { useState } from "react";
import { Loader2, Plus, UserCog } from "lucide-react";
import { useTranslation } from "react-i18next";
import { Alert } from "@/components/ui/alert";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { getErrorMessage } from "@/features/auth/lib/get-error-message";
import { useIamUsers } from "../hooks/use-iam";
import { ManageRolesDialog } from "./manage-roles-dialog";
import { UserCreateDialog } from "./user-create-dialog";
import type { IamUser } from "../types/iam";

export function UsersPanel() {
  const { t } = useTranslation();
  const { data: users, isLoading, isError, error } = useIamUsers();
  const [createOpen, setCreateOpen] = useState(false);
  const [managing, setManaging] = useState<IamUser | undefined>();

  return (
    <div className="flex flex-col gap-4">
      <div className="flex items-center justify-between gap-4">
        <p className="text-sm text-muted-foreground">{t("iam.users.hint")}</p>
        <Button onClick={() => setCreateOpen(true)} className="gap-1.5">
          <Plus className="size-4" />
          {t("iam.users.addUser")}
        </Button>
      </div>

      {isError && (
        <Alert>
          <span>{getErrorMessage(error)}</span>
        </Alert>
      )}

      <div className="overflow-x-auto rounded-xl border border-border">
        <table className="w-full text-sm">
          <thead>
            <tr className="border-b border-border bg-muted/40 text-left text-xs tracking-wider text-muted-foreground uppercase">
              <th className="px-4 py-2.5 font-medium">
                {t("iam.users.columns.username")}
              </th>
              <th className="px-4 py-2.5 font-medium">
                {t("iam.users.columns.email")}
              </th>
              <th className="px-4 py-2.5 font-medium">
                {t("iam.users.columns.roles")}
              </th>
              <th className="px-4 py-2.5" />
            </tr>
          </thead>
          <tbody>
            {isLoading && (
              <tr>
                <td colSpan={4} className="px-4 py-10 text-center">
                  <Loader2 className="mx-auto size-5 animate-spin text-muted-foreground" />
                </td>
              </tr>
            )}
            {!isLoading && (users?.length ?? 0) === 0 && (
              <tr>
                <td
                  colSpan={4}
                  className="px-4 py-10 text-center text-muted-foreground"
                >
                  {t("iam.users.empty")}
                </td>
              </tr>
            )}
            {!isLoading &&
              users?.map((user) => (
                <tr
                  key={user.id}
                  className="border-b border-border last:border-0 hover:bg-muted/30"
                >
                  <td className="px-4 py-2.5 font-medium">
                    {user.username}
                    {!user.enabled && (
                      <span className="ml-2 text-xs text-muted-foreground">
                        {t("iam.users.disabled")}
                      </span>
                    )}
                  </td>
                  <td className="px-4 py-2.5 text-muted-foreground">
                    {user.email}
                  </td>
                  <td className="px-4 py-2.5">
                    <div className="flex flex-wrap gap-1">
                      {user.roles.length === 0 ? (
                        <span className="text-muted-foreground">—</span>
                      ) : (
                        user.roles.map((role) => (
                          <Badge key={role} variant="primary">
                            {role}
                          </Badge>
                        ))
                      )}
                    </div>
                  </td>
                  <td className="px-4 py-2.5">
                    <div className="flex justify-end">
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => setManaging(user)}
                        className="gap-1.5"
                      >
                        <UserCog className="size-3.5" />
                        {t("iam.users.manageRoles")}
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
          </tbody>
        </table>
      </div>

      <UserCreateDialog open={createOpen} onOpenChange={setCreateOpen} />
      <ManageRolesDialog
        user={managing}
        open={Boolean(managing)}
        onOpenChange={(next) => !next && setManaging(undefined)}
      />
    </div>
  );
}
