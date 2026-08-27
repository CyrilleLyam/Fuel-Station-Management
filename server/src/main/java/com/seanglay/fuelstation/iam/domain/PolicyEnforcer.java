package com.seanglay.fuelstation.iam.domain;

import java.util.List;

public interface PolicyEnforcer {

	boolean hasPermission(String username, String resource, String action);

	void grantPermissionToRole(String role, String resource, String action);

	void revokePermissionFromRole(String role, String resource, String action);

	void assignRoleToUser(String username, String role);

	void unassignRoleFromUser(String username, String role);

	List<String> getRolesForUser(String username);

	List<Permission> getPermissionsForUser(String username);

	List<String> getAllRoles();

	List<String> getUsersForRole(String role);

	List<Permission> getPermissionsForRole(String role);

}
