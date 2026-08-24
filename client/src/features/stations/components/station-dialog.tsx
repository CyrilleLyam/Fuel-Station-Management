import { Dialog, DialogContent } from "@/components/ui/dialog";
import { StationForm } from "./station-form";
import type { Station } from "../types/station";

export function StationDialog({
  station,
  open,
  onOpenChange,
}: {
  station?: Station;
  open: boolean;
  onOpenChange: (open: boolean) => void;
}) {
  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-xl gap-0 p-0">
        {open && (
          <StationForm
            station={station}
            onSuccess={() => onOpenChange(false)}
          />
        )}
      </DialogContent>
    </Dialog>
  );
}
