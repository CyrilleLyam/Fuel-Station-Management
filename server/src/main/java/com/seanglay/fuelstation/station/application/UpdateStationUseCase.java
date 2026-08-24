package com.seanglay.fuelstation.station.application;

import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.NotFoundException;
import com.seanglay.fuelstation.station.domain.Station;
import com.seanglay.fuelstation.station.domain.StationRepository;

@UseCase
public class UpdateStationUseCase {

	private final StationRepository stationRepository;

	public UpdateStationUseCase(StationRepository stationRepository) {
		this.stationRepository = stationRepository;
	}

	public Station execute(Long id, String name, String address, String phone, Double latitude, Double longitude,
			boolean enabled) {
		Station station = stationRepository.findById(id)
			.orElseThrow(() -> new NotFoundException("Station not found: " + id));

		station.update(name, address, phone, latitude, longitude);

		if (enabled) {
			station.enable();
		}
		else {
			station.disable();
		}

		return stationRepository.save(station);
	}

}
