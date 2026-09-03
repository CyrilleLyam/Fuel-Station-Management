import { Dialog, DialogContent } from "@/components/ui/dialog";
import { SaleForm } from "./sale-form";

export function SaleDialog({
  open,
  onOpenChange,
}: {
  open: boolean;
  onOpenChange: (open: boolean) => void;
}) {
  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-xl gap-0 p-0">
        {open && <SaleForm onSuccess={() => onOpenChange(false)} />}
      </DialogContent>
    </Dialog>
  );
}
