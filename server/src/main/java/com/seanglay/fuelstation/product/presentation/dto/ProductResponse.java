package com.seanglay.fuelstation.product.presentation.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.seanglay.fuelstation.product.domain.FuelType;
import com.seanglay.fuelstation.product.domain.Product;

public record ProductResponse(Long id, String name, String sku, FuelType fuelType, String unit, BigDecimal unitPrice,
		boolean active, Instant createdAt, Instant updatedAt) {

	public static ProductResponse from(Product product) {
		return new ProductResponse(product.getId(), product.getName(), product.getSku(), product.getFuelType(),
				product.getUnit(), product.getUnitPrice(), product.isActive(), product.getCreatedAt(),
				product.getUpdatedAt());
	}

}
