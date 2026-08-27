package com.seanglay.fuelstation.iam.presentation;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seanglay.fuelstation.iam.application.GetCurrentUserUseCase;
import com.seanglay.fuelstation.iam.presentation.dto.UserResponse;
import com.seanglay.fuelstation.shared.presentation.ApiResponse;

@RestController
@RequestMapping("/users")
class UserController {

	private final GetCurrentUserUseCase getCurrentUserUseCase;

	UserController(GetCurrentUserUseCase getCurrentUserUseCase) {
		this.getCurrentUserUseCase = getCurrentUserUseCase;
	}

	@GetMapping("/me")
	ApiResponse<UserResponse> me(Authentication authentication) {
		GetCurrentUserUseCase.Result result = getCurrentUserUseCase.execute(authentication.getName());
		return ApiResponse.ok("Current user", UserResponse.from(result));
	}

}
