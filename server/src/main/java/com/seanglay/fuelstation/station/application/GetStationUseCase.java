package com.seanglay.fuelstation.station.application;

import org.springframework.transaction.annotation.Transactional;

import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.NotFoundException;
import com.seanglay.fuelstation.station.domain.Station;
import com.seanglay.fuelstation.station.domain.StationRepository;

@UseCase
public class GetStationUseCase {

	private final StationRepository stationRepository;

	public GetStationUseCase(StationRepository stationRepository) {
		this.stationRepository = stationRepository;
	}

	@Transactional(readOnly = true)
	public Station execute(Long id) {
		return stationRepository.findById(id)
			.orElseThrow(() -> new NotFoundException("Station not found: " + id));
	}

}
