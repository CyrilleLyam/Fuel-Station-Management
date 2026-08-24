package com.seanglay.fuelstation.station.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateStationRequest(@NotBlank String name, @NotBlank String code, String address, String phone,
		Double latitude, Double longitude) {
}
