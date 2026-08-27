import { useState } from "react";
import { Loader2, Plus, X } from "lucide-react";
import { useTranslation } from "react-i18next";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { showToast } from "@/lib/toast";
import { useAssignRole, useUnassignRole } from "../hooks/use-iam-mutations";
import { useIamRoles } from "../hooks/use-iam";
import type { IamUser } from "../types/iam";

export function ManageRolesDialog({
  user,
  open,
  onOpenChange,
}: {
  user?: IamUser;
  open: boolean;
  onOpenChange: (open: boolean) => void;
}) {
  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-md gap-0 p-0">
        {open && user && <ManageRolesBody user={user} />}
      </DialogContent>
    </Dialog>
  );
}

function ManageRolesBody({ user }: { user: IamUser }) {
  const { t } = useTranslation();
  const { data: roles } = useIamRoles();
  const assignRole = useAssignRole();
  const unassignRole = useUnassignRole();
  const [newRole, setNewRole] = useState("");

  const knownRoles = (roles ?? []).map((role) => role.name);
  const suggestions = knownRoles.filter((role) => !user.roles.includes(role));
  const pending = assignRole.isPending || unassignRole.isPending;

  function add(role: string) {
    const value = role.trim().toUpperCase();
    if (!value || user.roles.includes(value)) {
      return;
    }
    assignRole.mutate(
      { username: user.username, role: value },
      {
        onSuccess: () => {
          showToast(t("iam.roles.assigned", { role: value }), "success");
          setNewRole("");
        },
      },
    );
  }

  function remove(role: string) {
    unassignRole.mutate(
      { username: user.username, role },
      {
        onSuccess: () =>
          showToast(t("iam.roles.unassigned", { role }), "success"),
      },
    );
  }

  return (
    <div className="flex flex-col">
      <DialogHeader>
        <DialogTitle>{t("iam.roles.manageTitle")}</DialogTitle>
        <DialogDescription>
          {t("iam.roles.manageDescription", { username: user.username })}
        </DialogDescription>
      </DialogHeader>

      <div className="flex flex-col gap-4 px-6 py-5">
        <div className="flex flex-col gap-2">
          <span className="font-jetbrains text-xs tracking-[0.15em] text-muted-foreground uppercase">
            {t("iam.roles.current")}
          </span>
          <div className="flex flex-wrap gap-1.5">
            {user.roles.length === 0 && (
              <span className="text-sm text-muted-foreground">
                {t("iam.roles.none")}
              </span>
            )}
            {user.roles.map((role) => (
              <Badge key={role} variant="primary" className="pr-1">
                {role}
                <button
                  type="button"
                  onClick={() => remove(role)}
                  disabled={pending}
                  aria-label={t("iam.roles.remove", { role })}
                  className="rounded-full p-0.5 hover:bg-primary/20 disabled:opacity-50"
                >
                  <X className="size-3" />
                </button>
              </Badge>
            ))}
          </div>
        </div>

        <div className="flex flex-col gap-2">
          <span className="font-jetbrains text-xs tracking-[0.15em] text-muted-foreground uppercase">
            {t("iam.roles.addRole")}
          </span>
          <div className="flex gap-2">
            <Input
              value={newRole}
              onChange={(event) => setNewRole(event.target.value)}
              placeholder={t("iam.roles.rolePlaceholder")}
              onKeyDown={(event) => {
                if (event.key === "Enter") {
                  event.preventDefault();
                  add(newRole);
                }
              }}
            />
            <Button
              type="button"
              onClick={() => add(newRole)}
              disabled={pending || !newRole.trim()}
              className="relative shrink-0"
            >
              <span className={pending ? "invisible" : "inline-flex"}>
                <Plus className="size-4" />
              </span>
              {pending && (
                <Loader2 className="absolute inset-0 m-auto size-4 animate-spin" />
              )}
            </Button>
          </div>
          {suggestions.length > 0 && (
            <div className="flex flex-wrap gap-1.5">
              {suggestions.map((role) => (
                <button
                  key={role}
                  type="button"
                  onClick={() => add(role)}
                  disabled={pending}
                  className="rounded-full border border-border px-2 py-0.5 text-xs text-muted-foreground hover:bg-muted disabled:opacity-50"
                >
                  + {role}
                </button>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
