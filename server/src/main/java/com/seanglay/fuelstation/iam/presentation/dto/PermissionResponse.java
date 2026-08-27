package com.seanglay.fuelstation.iam.presentation.dto;

import com.seanglay.fuelstation.iam.domain.Permission;

public record PermissionResponse(String resource, String action) {

	public static PermissionResponse from(Permission permission) {
		return new PermissionResponse(permission.resource(), permission.action());
	}

}
