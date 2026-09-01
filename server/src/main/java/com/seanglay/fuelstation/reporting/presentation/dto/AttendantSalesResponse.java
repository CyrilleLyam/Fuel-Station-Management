package com.seanglay.fuelstation.reporting.presentation.dto;

import java.math.BigDecimal;

import com.seanglay.fuelstation.reporting.domain.AttendantSalesRow;

public record AttendantSalesResponse(String attendant, BigDecimal quantity, BigDecimal totalAmount, Long transactions) {

	public static AttendantSalesResponse from(AttendantSalesRow row) {
		return new AttendantSalesResponse(row.attendant(), row.quantity(), row.totalAmount(), row.transactions());
	}

}
