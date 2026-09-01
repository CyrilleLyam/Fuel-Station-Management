package com.seanglay.fuelstation.reporting.domain;

import java.math.BigDecimal;

public record AttendantSalesRow(String attendant, BigDecimal quantity, BigDecimal totalAmount, Long transactions) {
}
