package com.seanglay.fuelstation.iam.infrastructure;

import java.util.List;

import org.casbin.jcasbin.main.Enforcer;
import org.springframework.stereotype.Component;

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
		return enforcer.getRolesForUser(username);
	}

}
