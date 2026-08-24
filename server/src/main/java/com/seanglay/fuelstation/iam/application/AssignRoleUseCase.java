package com.seanglay.fuelstation.iam.application;

import com.seanglay.fuelstation.iam.domain.PolicyEnforcer;
import com.seanglay.fuelstation.iam.domain.UserRepository;
import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.NotFoundException;

@UseCase
public class AssignRoleUseCase {

	private final UserRepository userRepository;

	private final PolicyEnforcer policyEnforcer;

	public AssignRoleUseCase(UserRepository userRepository, PolicyEnforcer policyEnforcer) {
		this.userRepository = userRepository;
		this.policyEnforcer = policyEnforcer;
	}

	public void assign(String username, String role) {
		requireExistingUser(username);
		policyEnforcer.assignRoleToUser(username, role);
	}

	public void unassign(String username, String role) {
		requireExistingUser(username);
		policyEnforcer.unassignRoleFromUser(username, role);
	}

	private void requireExistingUser(String username) {
		if (!userRepository.existsByUsername(username)) {
			throw new NotFoundException("No such user: " + username);
		}
	}

}
