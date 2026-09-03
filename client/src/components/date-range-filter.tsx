import { useTranslation } from "react-i18next";
import { Input } from "@/components/ui/input";

export interface DateRange {
  from: string;
  to: string;
}

export function DateRangeFilter({
  value,
  onChange,
}: {
  value: DateRange;
  onChange: (range: DateRange) => void;
}) {
  const { t } = useTranslation();

  return (
    <div className="flex items-end gap-2">
      <div className="flex flex-col gap-1.5">
        <label className="font-jetbrains text-xs tracking-[0.15em] text-muted-foreground uppercase">
          {t("common.from")}
        </label>
        <Input
          type="date"
          value={value.from}
          max={value.to || undefined}
          onChange={(event) => onChange({ ...value, from: event.target.value })}
          className="w-40"
        />
      </div>
      <div className="flex flex-col gap-1.5">
        <label className="font-jetbrains text-xs tracking-[0.15em] text-muted-foreground uppercase">
          {t("common.to")}
        </label>
        <Input
          type="date"
          value={value.to}
          min={value.from || undefined}
          onChange={(event) => onChange({ ...value, to: event.target.value })}
          className="w-40"
        />
      </div>
    </div>
  );
}
