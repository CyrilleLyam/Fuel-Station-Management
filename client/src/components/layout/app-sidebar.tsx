import { Link, useNavigate } from "@tanstack/react-router";
import { Fuel, LayoutDashboard, Receipt, Settings, Users } from "lucide-react";
import { Button } from "@/components/ui/button";
import { ThemeToggle } from "@/components/theme-toggle";
import { useCurrentUser } from "@/features/auth/hooks/use-current-user";
import { clearTokens } from "@/features/auth/lib/token-storage";

const NAV_ITEMS = [{ label: "Dashboard", to: "/" as const, icon: LayoutDashboard }];

const SOON_ITEMS = [
  { label: "Stations", icon: Fuel },
  { label: "Transactions", icon: Receipt },
  { label: "Staff", icon: Users },
  { label: "Settings", icon: Settings },
];

export function AppSidebar() {
  const navigate = useNavigate();
  const { data: user } = useCurrentUser();

  function handleLogout() {
    clearTokens();
    navigate({ to: "/login" });
  }

  return (
    <aside className="flex w-64 shrink-0 flex-col border-r border-sidebar-border bg-sidebar text-sidebar-foreground">
      <div className="flex items-center justify-between px-5 py-5">
        <span className="font-display text-sm font-medium tracking-tight uppercase">
          Fuel Station
        </span>
        <ThemeToggle />
      </div>
      <nav className="flex flex-1 flex-col gap-1 px-3">
        {NAV_ITEMS.map((item) => (
          <Link
            key={item.label}
            to={item.to}
            activeOptions={{ exact: true }}
            className="flex items-center gap-2.5 rounded-md px-3 py-2 text-sm font-medium text-sidebar-foreground hover:bg-sidebar-accent hover:text-sidebar-accent-foreground"
            activeProps={{
              className: "bg-sidebar-accent text-sidebar-accent-foreground",
            }}
          >
            <item.icon className="size-4" />
            {item.label}
          </Link>
        ))}
        <div className="mt-2 flex flex-col gap-1">
          {SOON_ITEMS.map((item) => (
            <div
              key={item.label}
              className="flex items-center justify-between gap-2.5 rounded-md px-3 py-2 text-sm text-muted-foreground/60"
            >
              <span className="flex items-center gap-2.5">
                <item.icon className="size-4" />
                {item.label}
              </span>
              <span className="font-jetbrains text-[10px] tracking-wider uppercase">
                Soon
              </span>
            </div>
          ))}
        </div>
      </nav>
      <div className="flex flex-col gap-3 border-t border-sidebar-border px-4 py-4">
        <div className="flex flex-col">
          <span className="truncate text-sm font-medium">
            {user?.username ?? "…"}
          </span>
          <span className="truncate text-xs text-muted-foreground">
            {user?.email ?? ""}
          </span>
        </div>
        <Button variant="outline" size="sm" onClick={handleLogout}>
          Log out
        </Button>
      </div>
    </aside>
  );
}
