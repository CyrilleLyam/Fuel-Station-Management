package com.seanglay.fuelstation.iam.application;

import java.util.List;

import com.seanglay.fuelstation.iam.domain.Permission;
import com.seanglay.fuelstation.iam.domain.PolicyEnforcer;
import com.seanglay.fuelstation.shared.application.UseCase;

@UseCase
public class ListRolesUseCase {

	private final PolicyEnforcer policyEnforcer;

	public ListRolesUseCase(PolicyEnforcer policyEnforcer) {
		this.policyEnforcer = policyEnforcer;
	}

	public List<Result> execute() {
		return policyEnforcer.getAllRoles()
			.stream()
			.map(role -> new Result(role, policyEnforcer.getPermissionsForRole(role),
					policyEnforcer.getUsersForRole(role)))
			.toList();
	}

	public record Result(String name, List<Permission> permissions, List<String> users) {
	}

}
