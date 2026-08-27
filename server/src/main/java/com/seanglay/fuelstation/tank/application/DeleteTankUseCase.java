package com.seanglay.fuelstation.tank.application;

import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.NotFoundException;
import com.seanglay.fuelstation.tank.domain.TankRepository;

@UseCase
public class DeleteTankUseCase {

	private final TankRepository tankRepository;

	public DeleteTankUseCase(TankRepository tankRepository) {
		this.tankRepository = tankRepository;
	}

	public void execute(Long id) {
		if (!tankRepository.findById(id).isPresent()) {
			throw new NotFoundException("Tank not found: " + id);
		}

		tankRepository.deleteById(id);
	}

}
