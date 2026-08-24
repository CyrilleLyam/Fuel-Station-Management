package com.seanglay.fuelstation.iam.application;

import com.seanglay.fuelstation.iam.domain.PasswordHasher;
import com.seanglay.fuelstation.iam.domain.TokenIssuer;
import com.seanglay.fuelstation.iam.domain.User;
import com.seanglay.fuelstation.iam.domain.UserRepository;
import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.UnauthorizedException;

@UseCase
public class LoginUseCase {

	private static final String INVALID_CREDENTIALS_MESSAGE = "Invalid username or password";

	private final UserRepository userRepository;

	private final PasswordHasher passwordHasher;

	private final TokenIssuer tokenIssuer;

	public LoginUseCase(UserRepository userRepository, PasswordHasher passwordHasher, TokenIssuer tokenIssuer) {
		this.userRepository = userRepository;
		this.passwordHasher = passwordHasher;
		this.tokenIssuer = tokenIssuer;
	}

	public record Result(String accessToken, String refreshToken) {
	}

	public Result execute(String username, String rawPassword) {
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new UnauthorizedException(INVALID_CREDENTIALS_MESSAGE));

		if (!user.isEnabled() || !passwordHasher.matches(rawPassword, user.getPassword())) {
			throw new UnauthorizedException(INVALID_CREDENTIALS_MESSAGE);
		}

		return new Result(tokenIssuer.issueAccessToken(user), tokenIssuer.issueRefreshToken(user));
	}

}
