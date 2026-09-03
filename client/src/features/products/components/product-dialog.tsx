import { Dialog, DialogContent } from "@/components/ui/dialog";
import { ProductForm } from "./product-form";
import type { Product } from "../types/product";

export function ProductDialog({
  product,
  open,
  onOpenChange,
}: {
  product?: Product;
  open: boolean;
  onOpenChange: (open: boolean) => void;
}) {
  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-xl gap-0 p-0">
        {open && (
          <ProductForm product={product} onSuccess={() => onOpenChange(false)} />
        )}
      </DialogContent>
    </Dialog>
  );
}
