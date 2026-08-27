package com.seanglay.fuelstation.tank.presentation.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SetCapacityRequest(@NotNull @Positive BigDecimal capacity) {
}
