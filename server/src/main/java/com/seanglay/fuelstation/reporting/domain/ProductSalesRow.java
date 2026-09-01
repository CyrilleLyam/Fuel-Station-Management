package com.seanglay.fuelstation.reporting.domain;

import java.math.BigDecimal;

public record ProductSalesRow(Long productId, BigDecimal quantity, BigDecimal totalAmount, Long transactions) {
}
