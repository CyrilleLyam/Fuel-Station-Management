package com.seanglay.fuelstation.tank.application;

import java.math.BigDecimal;

import org.springframework.transaction.annotation.Transactional;

import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.NotFoundException;
import com.seanglay.fuelstation.tank.domain.Tank;
import com.seanglay.fuelstation.tank.domain.TankRepository;

@UseCase
public class ApplySaleToInventoryUseCase {

	private final TankRepository tankRepository;

	public ApplySaleToInventoryUseCase(TankRepository tankRepository) {
		this.tankRepository = tankRepository;
	}

	@Transactional
	public BigDecimal execute(Long tankId, Long productId, BigDecimal quantity) {
		Tank tank = tankRepository.findByIdForUpdate(tankId)
			.orElseThrow(() -> new NotFoundException("Tank not found: " + tankId));

		if (tank.getProductId() != null && !tank.getProductId().equals(productId)) {
			throw new IllegalStateException("Tank %d holds product %d but the sale was for product %d".formatted(tankId,
					tank.getProductId(), productId));
		}

		BigDecimal shortfall = tank.recordSale(quantity);
		tankRepository.save(tank);

		return shortfall;
	}

}
