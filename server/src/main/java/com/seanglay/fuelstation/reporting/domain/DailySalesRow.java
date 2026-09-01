package com.seanglay.fuelstation.reporting.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailySalesRow(LocalDate businessDate, BigDecimal quantity, BigDecimal totalAmount, Long transactions) {
}
