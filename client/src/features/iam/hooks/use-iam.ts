import { useQuery } from "@tanstack/react-query";
import { listIamRoles, listIamUsers } from "../api/iam.api";

export function useIamUsers() {
  return useQuery({
    queryKey: ["iam", "users"],
    queryFn: listIamUsers,
  });
}

export function useIamRoles() {
  return useQuery({
    queryKey: ["iam", "roles"],
    queryFn: listIamRoles,
  });
}
