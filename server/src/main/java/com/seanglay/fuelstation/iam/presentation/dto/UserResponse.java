package com.seanglay.fuelstation.iam.presentation.dto;

import java.util.UUID;

public record UserResponse(UUID id, String username, String email) {
}
