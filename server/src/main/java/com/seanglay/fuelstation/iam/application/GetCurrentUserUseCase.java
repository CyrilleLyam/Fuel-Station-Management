package com.seanglay.fuelstation.iam.application;

import com.seanglay.fuelstation.iam.domain.User;
import com.seanglay.fuelstation.iam.domain.UserRepository;
import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.UnauthorizedException;

@UseCase
public class GetCurrentUserUseCase {

	private final UserRepository userRepository;

	public GetCurrentUserUseCase(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User execute(String username) {
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new UnauthorizedException("Unknown user"));

		if (!user.isEnabled()) {
			throw new UnauthorizedException("User is disabled");
		}

		return user;
	}

}
