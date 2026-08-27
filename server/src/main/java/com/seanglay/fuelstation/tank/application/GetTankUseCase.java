package com.seanglay.fuelstation.tank.application;

import org.springframework.transaction.annotation.Transactional;

import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.NotFoundException;
import com.seanglay.fuelstation.tank.domain.Tank;
import com.seanglay.fuelstation.tank.domain.TankRepository;

@UseCase
public class GetTankUseCase {

	private final TankRepository tankRepository;

	public GetTankUseCase(TankRepository tankRepository) {
		this.tankRepository = tankRepository;
	}

	@Transactional(readOnly = true)
	public Tank execute(Long id) {
		return tankRepository.findById(id).orElseThrow(() -> new NotFoundException("Tank not found: " + id));
	}

}
