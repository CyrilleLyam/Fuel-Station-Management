package com.seanglay.fuelstation.tank.application;

import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.NotFoundException;
import com.seanglay.fuelstation.tank.domain.Tank;
import com.seanglay.fuelstation.tank.domain.TankRepository;

@UseCase
public class UpdateTankUseCase {

	private final TankRepository tankRepository;

	public UpdateTankUseCase(TankRepository tankRepository) {
		this.tankRepository = tankRepository;
	}

	public Tank execute(Long id, String label, boolean active) {
		Tank tank = tankRepository.findById(id).orElseThrow(() -> new NotFoundException("Tank not found: " + id));

		tank.rename(label);

		if (active) {
			tank.activate();
		}
		else {
			tank.deactivate();
		}

		return tankRepository.save(tank);
	}

}
