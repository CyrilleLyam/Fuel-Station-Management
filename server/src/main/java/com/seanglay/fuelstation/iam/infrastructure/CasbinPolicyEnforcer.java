package com.seanglay.fuelstation.iam.infrastructure;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import org.casbin.jcasbin.main.Enforcer;
import org.springframework.stereotype.Component;

import com.seanglay.fuelstation.iam.domain.Permission;
import com.seanglay.fuelstation.iam.domain.PolicyEnforcer;

@Component
class CasbinPolicyEnforcer implements PolicyEnforcer {

	private final Enforcer enforcer;

	CasbinPolicyEnforcer(Enforcer enforcer) {
		this.enforcer = enforcer;
	}

	@Override
	public boolean hasPermission(String username, String resource, String action) {
		return enforcer.enforce(username, resource, action);
	}

	@Override
	public void grantPermissionToRole(String role, String resource, String action) {
		enforcer.addPolicy(role, resource, action);
	}

	@Override
	public void revokePermissionFromRole(String role, String resource, String action) {
		enforcer.removePolicy(role, resource, action);
	}

	@Override
	public void assignRoleToUser(String username, String role) {
		enforcer.addRoleForUser(username, role);
	}

	@Override
	public void unassignRoleFromUser(String username, String role) {
		enforcer.deleteRoleForUser(username, role);
	}

	@Override
	public List<String> getRolesForUser(String username) {
		return safeList(() -> enforcer.getRolesForUser(username));
	}

	@Override
	public List<Permission> getPermissionsForUser(String username) {
		return toPermissions(safeRules(() -> enforcer.getImplicitPermissionsForUser(username)));
	}

	@Override
	public List<String> getAllRoles() {
		Set<String> roles = new LinkedHashSet<>(safeList(enforcer::getAllRoles));
		roles.addAll(safeList(enforcer::getAllSubjects));
		return List.copyOf(roles);
	}

	@Override
	public List<String> getUsersForRole(String role) {
		return safeList(() -> enforcer.getUsersForRole(role));
	}

	@Override
	public List<Permission> getPermissionsForRole(String role) {
		return toPermissions(safeRules(() -> enforcer.getFilteredPolicy(0, role)));
	}

	private static List<Permission> toPermissions(List<List<String>> rules) {
		return rules.stream()
			.filter(rule -> rule.size() >= 3)
			.map(rule -> new Permission(rule.get(1), rule.get(2)))
			.distinct()
			.toList();
	}

	/*
	 * jCasbin's role-manager lookups raise a bare java.lang.Error when asked about a name
	 * it has never seen, so every read is funnelled through these guards and degrades to
	 * an empty result instead of propagating.
	 */
	private static List<String> safeList(Supplier<List<String>> supplier) {
		try {
			return supplier.get();
		}
		catch (RuntimeException | Error ex) {
			return List.of();
		}
	}

	private static List<List<String>> safeRules(Supplier<List<List<String>>> supplier) {
		try {
			return supplier.get();
		}
		catch (RuntimeException | Error ex) {
			return List.of();
		}
	}

}
