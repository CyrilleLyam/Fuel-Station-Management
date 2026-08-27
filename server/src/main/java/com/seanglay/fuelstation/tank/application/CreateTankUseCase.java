package com.seanglay.fuelstation.tank.application;

import java.math.BigDecimal;

import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.tank.domain.Tank;
import com.seanglay.fuelstation.tank.domain.TankRepository;

@UseCase
public class CreateTankUseCase {

	private final TankRepository tankRepository;

	public CreateTankUseCase(TankRepository tankRepository) {
		this.tankRepository = tankRepository;
	}

	public Tank execute(Long stationId, String label, BigDecimal capacity, Long productId) {
		Tank tank = new Tank(stationId, label, capacity);

		if (productId != null) {
			tank.assignProduct(productId);
		}

		return tankRepository.save(tank);
	}

}
