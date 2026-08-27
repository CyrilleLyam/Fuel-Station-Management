package com.seanglay.fuelstation.iam.presentation.dto;

import java.util.List;
import java.util.UUID;

import com.seanglay.fuelstation.iam.application.GetCurrentUserUseCase;

public record UserResponse(UUID id, String username, String email, List<String> roles,
		List<PermissionResponse> permissions) {

	public static UserResponse from(GetCurrentUserUseCase.Result result) {
		return new UserResponse(result.user().getId(), result.user().getUsername(), result.user().getEmail(),
				result.roles(), result.permissions().stream().map(PermissionResponse::from).toList());
	}

}
