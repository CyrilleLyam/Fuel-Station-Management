package com.seanglay.fuelstation.iam.application;

import com.seanglay.fuelstation.iam.domain.PolicyEnforcer;
import com.seanglay.fuelstation.shared.application.UseCase;

@UseCase
public class ManagePermissionUseCase {

	private final PolicyEnforcer policyEnforcer;

	public ManagePermissionUseCase(PolicyEnforcer policyEnforcer) {
		this.policyEnforcer = policyEnforcer;
	}

	public void grant(String role, String resource, String action) {
		policyEnforcer.grantPermissionToRole(role, resource, action);
	}

	public void revoke(String role, String resource, String action) {
		policyEnforcer.revokePermissionFromRole(role, resource, action);
	}

}
