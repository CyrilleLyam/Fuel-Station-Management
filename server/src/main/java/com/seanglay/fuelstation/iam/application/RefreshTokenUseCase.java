package com.seanglay.fuelstation.iam.application;

import com.seanglay.fuelstation.iam.domain.TokenIssuer;
import com.seanglay.fuelstation.iam.domain.User;
import com.seanglay.fuelstation.iam.domain.UserRepository;
import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.UnauthorizedException;

@UseCase
public class RefreshTokenUseCase {

	private final UserRepository userRepository;

	private final TokenIssuer tokenIssuer;

	public RefreshTokenUseCase(UserRepository userRepository, TokenIssuer tokenIssuer) {
		this.userRepository = userRepository;
		this.tokenIssuer = tokenIssuer;
	}

	public String execute(String refreshToken) {
		String username = tokenIssuer.verifyRefreshToken(refreshToken);
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new UnauthorizedException("Unknown user"));

		if (!user.isEnabled()) {
			throw new UnauthorizedException("User is disabled");
		}

		return tokenIssuer.issueAccessToken(user);
	}

}
