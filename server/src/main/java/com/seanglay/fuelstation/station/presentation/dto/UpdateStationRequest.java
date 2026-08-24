package com.seanglay.fuelstation.station.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateStationRequest(@NotBlank String name, String address, String phone, Double latitude,
		Double longitude, boolean enabled) {
}
