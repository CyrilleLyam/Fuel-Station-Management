package com.seanglay.fuelstation.tank.presentation.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TankMovementRequest(@NotNull @Positive BigDecimal amount) {
}
