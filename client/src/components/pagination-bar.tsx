import { useTranslation } from "react-i18next";
import type { PageMeta } from "@/lib/api-response";
import { Button } from "@/components/ui/button";

export function PaginationBar({
  meta,
  isFetching,
  countLabel,
  onPageChange,
}: {
  meta: PageMeta;
  isFetching?: boolean;
  countLabel: string;
  onPageChange: (page: number) => void;
}) {
  const { t } = useTranslation();
  const canPrev = meta.page > 0;
  const canNext = meta.page + 1 < meta.totalPages;

  return (
    <div className="flex items-center justify-between text-sm text-muted-foreground">
      <span>
        {t("common.pageInfo", {
          page: meta.page + 1,
          totalPages: Math.max(meta.totalPages, 1),
        })}{" "}
        · {countLabel}
        {isFetching && ` · ${t("common.refreshing")}`}
      </span>
      <div className="flex gap-2">
        <Button
          variant="outline"
          size="sm"
          disabled={!canPrev}
          onClick={() => onPageChange(Math.max(meta.page - 1, 0))}
        >
          {t("common.previous")}
        </Button>
        <Button
          variant="outline"
          size="sm"
          disabled={!canNext}
          onClick={() => onPageChange(meta.page + 1)}
        >
          {t("common.next")}
        </Button>
      </div>
    </div>
  );
}
