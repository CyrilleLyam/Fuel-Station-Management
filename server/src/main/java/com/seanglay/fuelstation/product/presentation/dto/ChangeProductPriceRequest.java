package com.seanglay.fuelstation.product.presentation.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ChangeProductPriceRequest(@NotNull @PositiveOrZero BigDecimal unitPrice) {
}
