import { useMutation, useQueryClient } from "@tanstack/react-query";
import {
  assignTankProduct,
  createTank,
  deleteTank,
  recordTankDelivery,
  recordTankDispense,
  setTankCapacity,
  updateTank,
} from "../api/tank.api";
import type { UpdateTankInput } from "../types/tank";

function useTankInvalidator() {
  const queryClient = useQueryClient();
  return () => {
    queryClient.invalidateQueries({ queryKey: ["tanks"] });
  };
}

export function useCreateTank() {
  const invalidate = useTankInvalidator();
  return useMutation({
    mutationFn: createTank,
    onSuccess: invalidate,
  });
}

interface UpdateTankVars {
  id: number;
  input: UpdateTankInput;
}

export function useUpdateTank() {
  const invalidate = useTankInvalidator();
  return useMutation({
    mutationFn: ({ id, input }: UpdateTankVars) => updateTank(id, input),
    onSuccess: invalidate,
  });
}

interface AssignProductVars {
  id: number;
  productId: number;
}

export function useAssignTankProduct() {
  const invalidate = useTankInvalidator();
  return useMutation({
    mutationFn: ({ id, productId }: AssignProductVars) =>
      assignTankProduct(id, productId),
    onSuccess: invalidate,
  });
}

interface CapacityVars {
  id: number;
  capacity: number;
}

export function useSetTankCapacity() {
  const invalidate = useTankInvalidator();
  return useMutation({
    mutationFn: ({ id, capacity }: CapacityVars) => setTankCapacity(id, capacity),
    onSuccess: invalidate,
  });
}

interface MovementVars {
  id: number;
  amount: number;
}

export function useRecordTankDelivery() {
  const invalidate = useTankInvalidator();
  return useMutation({
    mutationFn: ({ id, amount }: MovementVars) => recordTankDelivery(id, amount),
    onSuccess: invalidate,
  });
}

export function useRecordTankDispense() {
  const invalidate = useTankInvalidator();
  return useMutation({
    mutationFn: ({ id, amount }: MovementVars) => recordTankDispense(id, amount),
    onSuccess: invalidate,
  });
}

export function useDeleteTank() {
  const invalidate = useTankInvalidator();
  return useMutation({
    mutationFn: deleteTank,
    onSuccess: invalidate,
  });
}
