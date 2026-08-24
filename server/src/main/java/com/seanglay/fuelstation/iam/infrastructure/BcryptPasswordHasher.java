package com.seanglay.fuelstation.iam.infrastructure;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.seanglay.fuelstation.iam.domain.PasswordHasher;

@Component
class BcryptPasswordHasher implements PasswordHasher {

	private final PasswordEncoder passwordEncoder;

	BcryptPasswordHasher(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public String hash(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}

	@Override
	public boolean matches(String rawPassword, String hashedPassword) {
		return passwordEncoder.matches(rawPassword, hashedPassword);
	}

}
