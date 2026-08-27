package com.seanglay.fuelstation.iam.application;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.seanglay.fuelstation.iam.domain.PolicyEnforcer;
import com.seanglay.fuelstation.iam.domain.User;
import com.seanglay.fuelstation.iam.domain.UserRepository;
import com.seanglay.fuelstation.shared.application.UseCase;

@UseCase
public class ListUsersUseCase {

	private final UserRepository userRepository;

	private final PolicyEnforcer policyEnforcer;

	public ListUsersUseCase(UserRepository userRepository, PolicyEnforcer policyEnforcer) {
		this.userRepository = userRepository;
		this.policyEnforcer = policyEnforcer;
	}

	@Transactional(readOnly = true)
	public List<Result> execute() {
		return userRepository.findAll()
			.stream()
			.map(user -> new Result(user, policyEnforcer.getRolesForUser(user.getUsername())))
			.toList();
	}

	public record Result(User user, List<String> roles) {
	}

}
