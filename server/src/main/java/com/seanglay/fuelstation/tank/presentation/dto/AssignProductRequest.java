package com.seanglay.fuelstation.tank.presentation.dto;

import jakarta.validation.constraints.NotNull;

public record AssignProductRequest(@NotNull Long productId) {
}
