package com.seanglay.fuelstation.reporting.presentation.dto;

import java.math.BigDecimal;

import com.seanglay.fuelstation.reporting.domain.ProductSalesRow;

public record ProductSalesResponse(Long productId, BigDecimal quantity, BigDecimal totalAmount, Long transactions) {

	public static ProductSalesResponse from(ProductSalesRow row) {
		return new ProductSalesResponse(row.productId(), row.quantity(), row.totalAmount(), row.transactions());
	}

}
