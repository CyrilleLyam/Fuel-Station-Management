package com.seanglay.fuelstation.station.application;

import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.AlreadyExistsException;
import com.seanglay.fuelstation.station.domain.Station;
import com.seanglay.fuelstation.station.domain.StationRepository;

@UseCase
public class CreateStationUseCase {

	private final StationRepository stationRepository;

	public CreateStationUseCase(StationRepository stationRepository) {
		this.stationRepository = stationRepository;
	}

	public Station execute(String name, String code, String address, String phone, Double latitude,
			Double longitude) {
		if (stationRepository.existsByCode(code)) {
			throw new AlreadyExistsException("Station code already taken: " + code);
		}

		Station station = new Station(name, code, address, phone, latitude, longitude);
		return stationRepository.save(station);
	}

}
