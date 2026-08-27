package com.seanglay.fuelstation.iam.presentation.dto;

import java.util.List;
import java.util.UUID;

import com.seanglay.fuelstation.iam.application.ListUsersUseCase;

public record AdminUserResponse(UUID id, String username, String email, boolean enabled, List<String> roles) {

	public static AdminUserResponse from(ListUsersUseCase.Result result) {
		return new AdminUserResponse(result.user().getId(), result.user().getUsername(), result.user().getEmail(),
				result.user().isEnabled(), result.roles());
	}

}
