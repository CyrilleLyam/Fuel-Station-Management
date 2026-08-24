package com.seanglay.fuelstation.iam.application;

import com.seanglay.fuelstation.iam.domain.PasswordHasher;
import com.seanglay.fuelstation.iam.domain.User;
import com.seanglay.fuelstation.iam.domain.UserRepository;
import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.AlreadyExistsException;
import com.seanglay.fuelstation.shared.domain.UuidV7;

@UseCase
public class CreateUserUseCase {

	private final UserRepository userRepository;

	private final PasswordHasher passwordHasher;

	public CreateUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher) {
		this.userRepository = userRepository;
		this.passwordHasher = passwordHasher;
	}

	public User execute(String username, String email, String rawPassword) {
		if (userRepository.existsByUsername(username)) {
			throw new AlreadyExistsException("Username already taken: " + username);
		}

		if (userRepository.existsByEmail(email)) {
			throw new AlreadyExistsException("Email already taken: " + email);
		}

		User user = new User(UuidV7.randomUUID(), username, email, passwordHasher.hash(rawPassword));
		return userRepository.save(user);
	}

}
