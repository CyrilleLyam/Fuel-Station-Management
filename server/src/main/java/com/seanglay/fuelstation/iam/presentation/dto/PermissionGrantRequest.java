package com.seanglay.fuelstation.iam.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record PermissionGrantRequest(@NotBlank String role, @NotBlank String resource, @NotBlank String action) {
}
