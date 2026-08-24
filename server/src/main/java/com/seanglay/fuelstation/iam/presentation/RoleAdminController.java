package com.seanglay.fuelstation.iam.presentation;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seanglay.fuelstation.iam.RequiresPermission;
import com.seanglay.fuelstation.iam.application.AssignRoleUseCase;
import com.seanglay.fuelstation.iam.application.CreateUserUseCase;
import com.seanglay.fuelstation.iam.application.ManagePermissionUseCase;
import com.seanglay.fuelstation.iam.presentation.dto.CreateUserRequest;
import com.seanglay.fuelstation.iam.presentation.dto.PermissionGrantRequest;
import com.seanglay.fuelstation.iam.presentation.dto.RoleAssignmentRequest;
import com.seanglay.fuelstation.shared.presentation.ApiResponse;

@RestController
@RequestMapping("/admin/iam")
@RequiresPermission(resource = "iam", action = "admin")
class RoleAdminController {

	private final CreateUserUseCase createUserUseCase;

	private final AssignRoleUseCase assignRoleUseCase;

	private final ManagePermissionUseCase managePermissionUseCase;

	RoleAdminController(CreateUserUseCase createUserUseCase, AssignRoleUseCase assignRoleUseCase,
			ManagePermissionUseCase managePermissionUseCase) {
		this.createUserUseCase = createUserUseCase;
		this.assignRoleUseCase = assignRoleUseCase;
		this.managePermissionUseCase = managePermissionUseCase;
	}

	@PostMapping("/users")
	ResponseEntity<ApiResponse<Void>> createUser(@Valid @RequestBody CreateUserRequest request) {
		createUserUseCase.execute(request.username(), request.email(), request.password());
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("User created", null));
	}

	@PostMapping("/roles/assign")
	ApiResponse<Void> assignRole(@Valid @RequestBody RoleAssignmentRequest request) {
		assignRoleUseCase.assign(request.username(), request.role());
		return ApiResponse.ok("Role assigned", null);
	}

	@DeleteMapping("/roles/assign")
	ApiResponse<Void> unassignRole(@Valid @RequestBody RoleAssignmentRequest request) {
		assignRoleUseCase.unassign(request.username(), request.role());
		return ApiResponse.ok("Role unassigned", null);
	}

	@PostMapping("/permissions")
	ApiResponse<Void> grantPermission(@Valid @RequestBody PermissionGrantRequest request) {
		managePermissionUseCase.grant(request.role(), request.resource(), request.action());
		return ApiResponse.ok("Permission granted", null);
	}

	@DeleteMapping("/permissions")
	ApiResponse<Void> revokePermission(@Valid @RequestBody PermissionGrantRequest request) {
		managePermissionUseCase.revoke(request.role(), request.resource(), request.action());
		return ApiResponse.ok("Permission revoked", null);
	}

}
