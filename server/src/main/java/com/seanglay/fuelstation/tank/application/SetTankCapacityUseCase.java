package com.seanglay.fuelstation.tank.application;

import java.math.BigDecimal;

import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.NotFoundException;
import com.seanglay.fuelstation.tank.domain.Tank;
import com.seanglay.fuelstation.tank.domain.TankRepository;

@UseCase
public class SetTankCapacityUseCase {

	private final TankRepository tankRepository;

	public SetTankCapacityUseCase(TankRepository tankRepository) {
		this.tankRepository = tankRepository;
	}

	public Tank execute(Long id, BigDecimal capacity) {
		Tank tank = tankRepository.findById(id).orElseThrow(() -> new NotFoundException("Tank not found: " + id));

		tank.setCapacity(capacity);

		return tankRepository.save(tank);
	}

}
