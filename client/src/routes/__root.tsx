import { createRootRoute, Outlet, useRouter } from "@tanstack/react-router";
import { TanStackRouterDevtools } from "@tanstack/react-router-devtools";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import { QueryErrorResetBoundary } from "@tanstack/react-query";
import { ErrorBoundary } from "react-error-boundary";
import { ErrorFallback } from "@/components/error-fallback";
import { Toaster } from "@/components/toaster";

export const Route = createRootRoute({
  component: RootComponent,
});

function RootComponent() {
  const router = useRouter();

  return (
    <QueryErrorResetBoundary>
      {({ reset }) => (
        <>
          <ErrorBoundary
            FallbackComponent={ErrorFallback}
            onReset={() => {
              reset();
              router.invalidate();
            }}
          >
            <Outlet />
          </ErrorBoundary>
          <Toaster />
          <TanStackRouterDevtools position="bottom-right" />
          <ReactQueryDevtools initialIsOpen={false} buttonPosition="bottom-left" />
        </>
      )}
    </QueryErrorResetBoundary>
  );
}
