package com.seanglay.fuelstation.sales;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record SaleCompleted(UUID reference, Long saleId, Long stationId, Long tankId, Long productId, String attendant,
		BigDecimal quantity, BigDecimal unitPrice, BigDecimal totalAmount, PaymentMethod paymentMethod,
		Instant occurredAt) {
}
