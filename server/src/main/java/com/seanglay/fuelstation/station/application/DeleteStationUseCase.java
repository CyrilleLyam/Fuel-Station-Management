package com.seanglay.fuelstation.station.application;

import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.NotFoundException;
import com.seanglay.fuelstation.station.domain.StationRepository;

@UseCase
public class DeleteStationUseCase {

	private final StationRepository stationRepository;

	public DeleteStationUseCase(StationRepository stationRepository) {
		this.stationRepository = stationRepository;
	}

	public void execute(Long id) {
		if (!stationRepository.findById(id).isPresent()) {
			throw new NotFoundException("Station not found: " + id);
		}

		stationRepository.deleteById(id);
	}

}
