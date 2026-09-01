package com.seanglay.fuelstation.reporting.presentation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.seanglay.fuelstation.reporting.domain.DailySalesRow;

public record DailySalesResponse(LocalDate businessDate, BigDecimal quantity, BigDecimal totalAmount,
		Long transactions) {

	public static DailySalesResponse from(DailySalesRow row) {
		return new DailySalesResponse(row.businessDate(), row.quantity(), row.totalAmount(), row.transactions());
	}

}
