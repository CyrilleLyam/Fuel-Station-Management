package com.seanglay.fuelstation.product.presentation.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import com.seanglay.fuelstation.product.domain.FuelType;

public record CreateProductRequest(@NotBlank String name, @NotBlank String sku, @NotNull FuelType fuelType,
		@NotBlank String unit, @NotNull @PositiveOrZero BigDecimal unitPrice) {
}
