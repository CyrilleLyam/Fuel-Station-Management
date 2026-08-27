package com.seanglay.fuelstation.tank.application;

import java.math.BigDecimal;

import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.NotFoundException;
import com.seanglay.fuelstation.tank.domain.Tank;
import com.seanglay.fuelstation.tank.domain.TankRepository;

@UseCase
public class RecordTankMovementUseCase {

	private final TankRepository tankRepository;

	public RecordTankMovementUseCase(TankRepository tankRepository) {
		this.tankRepository = tankRepository;
	}

	public Tank recordDelivery(Long id, BigDecimal amount) {
		Tank tank = load(id);
		tank.recordDelivery(amount);
		return tankRepository.save(tank);
	}

	public Tank recordDispense(Long id, BigDecimal amount) {
		Tank tank = load(id);
		tank.recordDispense(amount);
		return tankRepository.save(tank);
	}

	private Tank load(Long id) {
		return tankRepository.findById(id).orElseThrow(() -> new NotFoundException("Tank not found: " + id));
	}

}
