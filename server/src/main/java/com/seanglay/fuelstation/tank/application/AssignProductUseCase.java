package com.seanglay.fuelstation.tank.application;

import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.NotFoundException;
import com.seanglay.fuelstation.tank.domain.Tank;
import com.seanglay.fuelstation.tank.domain.TankRepository;

@UseCase
public class AssignProductUseCase {

	private final TankRepository tankRepository;

	public AssignProductUseCase(TankRepository tankRepository) {
		this.tankRepository = tankRepository;
	}

	public Tank execute(Long id, Long productId) {
		Tank tank = tankRepository.findById(id).orElseThrow(() -> new NotFoundException("Tank not found: " + id));

		tank.assignProduct(productId);

		return tankRepository.save(tank);
	}

}
