package com.seanglay.fuelstation.tank.presentation.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.seanglay.fuelstation.tank.domain.Tank;

public record TankResponse(Long id, Long stationId, Long productId, String label, BigDecimal capacity,
		BigDecimal currentQuantity, BigDecimal availableSpace, boolean active, Instant createdAt, Instant updatedAt) {

	public static TankResponse from(Tank tank) {
		return new TankResponse(tank.getId(), tank.getStationId(), tank.getProductId(), tank.getLabel(),
				tank.getCapacity(), tank.getCurrentQuantity(), tank.getAvailableSpace(), tank.isActive(),
				tank.getCreatedAt(), tank.getUpdatedAt());
	}

}
