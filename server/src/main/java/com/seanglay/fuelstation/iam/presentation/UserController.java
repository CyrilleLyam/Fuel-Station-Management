package com.seanglay.fuelstation.iam.presentation;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seanglay.fuelstation.iam.application.GetCurrentUserUseCase;
import com.seanglay.fuelstation.iam.domain.User;
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
		User user = getCurrentUserUseCase.execute(authentication.getName());
		return ApiResponse.ok("Current user", new UserResponse(user.getId(), user.getUsername(), user.getEmail()));
	}

}
