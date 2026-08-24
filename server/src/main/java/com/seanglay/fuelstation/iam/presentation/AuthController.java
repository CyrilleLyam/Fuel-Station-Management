package com.seanglay.fuelstation.iam.presentation;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seanglay.fuelstation.iam.application.LoginUseCase;
import com.seanglay.fuelstation.iam.application.RefreshTokenUseCase;
import com.seanglay.fuelstation.iam.presentation.dto.AccessTokenResponse;
import com.seanglay.fuelstation.iam.presentation.dto.LoginRequest;
import com.seanglay.fuelstation.iam.presentation.dto.RefreshRequest;
import com.seanglay.fuelstation.iam.presentation.dto.TokenResponse;
import com.seanglay.fuelstation.shared.presentation.ApiResponse;

@RestController
@RequestMapping("/auth")
class AuthController {

	private final LoginUseCase loginUseCase;

	private final RefreshTokenUseCase refreshTokenUseCase;

	AuthController(LoginUseCase loginUseCase, RefreshTokenUseCase refreshTokenUseCase) {
		this.loginUseCase = loginUseCase;
		this.refreshTokenUseCase = refreshTokenUseCase;
	}

	@PostMapping("/login")
	ApiResponse<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
		LoginUseCase.Result result = loginUseCase.execute(request.username(), request.password());
		return ApiResponse.ok("Login successful", new TokenResponse(result.accessToken(), result.refreshToken()));
	}

	@PostMapping("/refresh")
	ApiResponse<AccessTokenResponse> refresh(@Valid @RequestBody RefreshRequest request) {
		String accessToken = refreshTokenUseCase.execute(request.refreshToken());
		return ApiResponse.ok("Token refreshed", new AccessTokenResponse(accessToken));
	}

}
