export interface ToastMessage {
  id: number;
  message: string;
  variant: "error" | "success";
}

let toasts: ToastMessage[] = [];
const listeners = new Set<() => void>();
let nextId = 1;

function emit() {
  for (const listener of listeners) {
    listener();
  }
}

export function subscribeToasts(listener: () => void) {
  listeners.add(listener);
  return () => {
    listeners.delete(listener);
  };
}

export function getToasts() {
  return toasts;
}

export function dismissToast(id: number) {
  toasts = toasts.filter((toast) => toast.id !== id);
  emit();
}

export function showToast(
  message: string,
  variant: ToastMessage["variant"] = "error",
) {
  const id = nextId++;
  toasts = [...toasts, { id, message, variant }];
  emit();
  setTimeout(() => dismissToast(id), 5000);
}
