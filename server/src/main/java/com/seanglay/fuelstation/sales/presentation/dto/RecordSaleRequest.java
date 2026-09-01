package com.seanglay.fuelstation.sales.presentation.dto;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import com.seanglay.fuelstation.sales.PaymentMethod;

public record RecordSaleRequest(@NotNull Long stationId, @NotNull Long tankId, @NotNull Long productId,
		@NotNull @Positive BigDecimal quantity, @NotNull PaymentMethod paymentMethod, Instant soldAt) {
}
