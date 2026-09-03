import { Dialog, DialogContent } from "@/components/ui/dialog";
import { TankForm } from "./tank-form";
import type { Tank } from "../types/tank";

export function TankDialog({
  tank,
  open,
  onOpenChange,
}: {
  tank?: Tank;
  open: boolean;
  onOpenChange: (open: boolean) => void;
}) {
  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-xl gap-0 p-0">
        {open && <TankForm tank={tank} onSuccess={() => onOpenChange(false)} />}
      </DialogContent>
    </Dialog>
  );
}
