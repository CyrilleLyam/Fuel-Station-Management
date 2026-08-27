package com.seanglay.fuelstation.iam.application;

import java.util.List;

import com.seanglay.fuelstation.iam.domain.Permission;
import com.seanglay.fuelstation.iam.domain.PolicyEnforcer;
import com.seanglay.fuelstation.iam.domain.User;
import com.seanglay.fuelstation.iam.domain.UserRepository;
import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.UnauthorizedException;

@UseCase
public class GetCurrentUserUseCase {

	private final UserRepository userRepository;

	private final PolicyEnforcer policyEnforcer;

	public GetCurrentUserUseCase(UserRepository userRepository, PolicyEnforcer policyEnforcer) {
		this.userRepository = userRepository;
		this.policyEnforcer = policyEnforcer;
	}

	public Result execute(String username) {
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new UnauthorizedException("Unknown user"));

		if (!user.isEnabled()) {
			throw new UnauthorizedException("User is disabled");
		}

		return new Result(user, policyEnforcer.getRolesForUser(username),
				policyEnforcer.getPermissionsForUser(username));
	}

	public record Result(User user, List<String> roles, List<Permission> permissions) {
	}

}
