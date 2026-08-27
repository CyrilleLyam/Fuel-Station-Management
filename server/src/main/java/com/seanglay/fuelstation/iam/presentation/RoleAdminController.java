package com.seanglay.fuelstation.iam.presentation;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seanglay.fuelstation.iam.RequiresPermission;
import com.seanglay.fuelstation.iam.application.AssignRoleUseCase;
import com.seanglay.fuelstation.iam.application.CreateUserUseCase;
import com.seanglay.fuelstation.iam.application.ListRolesUseCase;
import com.seanglay.fuelstation.iam.application.ListUsersUseCase;
import com.seanglay.fuelstation.iam.application.ManagePermissionUseCase;
import com.seanglay.fuelstation.iam.presentation.dto.AdminUserResponse;
import com.seanglay.fuelstation.iam.presentation.dto.CreateUserRequest;
import com.seanglay.fuelstation.iam.presentation.dto.PermissionGrantRequest;
import com.seanglay.fuelstation.iam.presentation.dto.RoleAssignmentRequest;
import com.seanglay.fuelstation.iam.presentation.dto.RoleResponse;
import com.seanglay.fuelstation.shared.presentation.ApiResponse;

@RestController
@RequestMapping("/admin/iam")
@RequiresPermission(resource = "iam", action = "admin")
class RoleAdminController {

	private final CreateUserUseCase createUserUseCase;

	private final AssignRoleUseCase assignRoleUseCase;

	private final ManagePermissionUseCase managePermissionUseCase;

	private final ListUsersUseCase listUsersUseCase;

	private final ListRolesUseCase listRolesUseCase;

	RoleAdminController(CreateUserUseCase createUserUseCase, AssignRoleUseCase assignRoleUseCase,
			ManagePermissionUseCase managePermissionUseCase, ListUsersUseCase listUsersUseCase,
			ListRolesUseCase listRolesUseCase) {
		this.createUserUseCase = createUserUseCase;
		this.assignRoleUseCase = assignRoleUseCase;
		this.managePermissionUseCase = managePermissionUseCase;
		this.listUsersUseCase = listUsersUseCase;
		this.listRolesUseCase = listRolesUseCase;
	}

	@GetMapping("/users")
	ApiResponse<List<AdminUserResponse>> listUsers() {
		List<AdminUserResponse> users = listUsersUseCase.execute().stream().map(AdminUserResponse::from).toList();
		return ApiResponse.ok("Users retrieved", users);
	}

	@GetMapping("/roles")
	ApiResponse<List<RoleResponse>> listRoles() {
		List<RoleResponse> roles = listRolesUseCase.execute().stream().map(RoleResponse::from).toList();
		return ApiResponse.ok("Roles retrieved", roles);
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
