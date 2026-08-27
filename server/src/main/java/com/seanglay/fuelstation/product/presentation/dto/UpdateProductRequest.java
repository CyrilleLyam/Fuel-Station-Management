package com.seanglay.fuelstation.product.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.seanglay.fuelstation.product.domain.FuelType;

public record UpdateProductRequest(@NotBlank String name, @NotNull FuelType fuelType, @NotBlank String unit,
		boolean active) {
}
