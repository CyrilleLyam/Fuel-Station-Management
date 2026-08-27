package com.seanglay.fuelstation.tank.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateTankRequest(@NotBlank String label, boolean active) {
}
