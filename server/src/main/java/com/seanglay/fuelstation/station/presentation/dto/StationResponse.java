package com.seanglay.fuelstation.station.presentation.dto;

import java.time.Instant;

import com.seanglay.fuelstation.station.domain.Station;

public record StationResponse(Long id, String name, String code, String address, String phone, Double latitude,
		Double longitude, boolean enabled, Instant createdAt, Instant updatedAt) {

	public static StationResponse from(Station station) {
		return new StationResponse(station.getId(), station.getName(), station.getCode(), station.getAddress(),
				station.getPhone(), station.getLatitude(), station.getLongitude(), station.isEnabled(),
				station.getCreatedAt(), station.getUpdatedAt());
	}

}
