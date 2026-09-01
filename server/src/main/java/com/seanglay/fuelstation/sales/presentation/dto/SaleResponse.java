package com.seanglay.fuelstation.sales.presentation.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.seanglay.fuelstation.sales.PaymentMethod;
import com.seanglay.fuelstation.sales.domain.Sale;

public record SaleResponse(Long id, UUID reference, Long stationId, Long tankId, Long productId, String attendant,
		BigDecimal quantity, BigDecimal unitPrice, BigDecimal totalAmount, PaymentMethod paymentMethod, Instant soldAt,
		Instant createdAt, Instant updatedAt) {

	public static SaleResponse from(Sale sale) {
		return new SaleResponse(sale.getId(), sale.getReference(), sale.getStationId(), sale.getTankId(),
				sale.getProductId(), sale.getAttendant(), sale.getQuantity(), sale.getUnitPrice(),
				sale.getTotalAmount(), sale.getPaymentMethod(), sale.getSoldAt(), sale.getCreatedAt(),
				sale.getUpdatedAt());
	}

}
