package com.seanglay.fuelstation.iam.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record RoleAssignmentRequest(@NotBlank String username, @NotBlank String role) {
}
