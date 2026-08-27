package com.seanglay.fuelstation.iam.presentation.dto;

import java.util.List;

import com.seanglay.fuelstation.iam.application.ListRolesUseCase;

public record RoleResponse(String name, List<PermissionResponse> permissions, List<String> users) {

	public static RoleResponse from(ListRolesUseCase.Result result) {
		return new RoleResponse(result.name(), result.permissions().stream().map(PermissionResponse::from).toList(),
				result.users());
	}

}
