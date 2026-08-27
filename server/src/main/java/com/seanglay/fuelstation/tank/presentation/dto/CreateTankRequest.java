package com.seanglay.fuelstation.tank.presentation.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateTankRequest(@NotNull Long stationId, @NotBlank String label, @NotNull @Positive BigDecimal capacity,
		Long productId) {
}
