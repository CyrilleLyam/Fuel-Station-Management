package com.seanglay.fuelstation.station.presentation;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seanglay.fuelstation.iam.RequiresPermission;
import com.seanglay.fuelstation.shared.domain.CursorPageResult;
import com.seanglay.fuelstation.shared.domain.PageResult;
import com.seanglay.fuelstation.shared.presentation.ApiResponse;
import com.seanglay.fuelstation.shared.presentation.CursorMeta;
import com.seanglay.fuelstation.shared.presentation.PageMeta;
import com.seanglay.fuelstation.station.application.CreateStationUseCase;
import com.seanglay.fuelstation.station.application.DeleteStationUseCase;
import com.seanglay.fuelstation.station.application.GetStationUseCase;
import com.seanglay.fuelstation.station.application.ListStationsUseCase;
import com.seanglay.fuelstation.station.application.UpdateStationUseCase;
import com.seanglay.fuelstation.station.domain.Station;
import com.seanglay.fuelstation.station.presentation.dto.CreateStationRequest;
import com.seanglay.fuelstation.station.presentation.dto.StationResponse;
import com.seanglay.fuelstation.station.presentation.dto.UpdateStationRequest;

@RestController
@RequestMapping("/stations")
class StationController {

	private final CreateStationUseCase createStationUseCase;

	private final GetStationUseCase getStationUseCase;

	private final ListStationsUseCase listStationsUseCase;

	private final UpdateStationUseCase updateStationUseCase;

	private final DeleteStationUseCase deleteStationUseCase;

	StationController(CreateStationUseCase createStationUseCase, GetStationUseCase getStationUseCase,
			ListStationsUseCase listStationsUseCase, UpdateStationUseCase updateStationUseCase,
			DeleteStationUseCase deleteStationUseCase) {
		this.createStationUseCase = createStationUseCase;
		this.getStationUseCase = getStationUseCase;
		this.listStationsUseCase = listStationsUseCase;
		this.updateStationUseCase = updateStationUseCase;
		this.deleteStationUseCase = deleteStationUseCase;
	}

	@PostMapping
	@RequiresPermission(resource = "station", action = "create")
	ResponseEntity<ApiResponse<StationResponse>> create(@Valid @RequestBody CreateStationRequest request) {
		Station station = createStationUseCase.execute(request.name(), request.code(), request.address(),
				request.phone(), request.latitude(), request.longitude());
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ApiResponse.ok("Station created", StationResponse.from(station)));
	}

	@GetMapping("/{id}")
	@RequiresPermission(resource = "station", action = "read")
	ApiResponse<StationResponse> get(@PathVariable Long id) {
		Station station = getStationUseCase.execute(id);
		return ApiResponse.ok("Station retrieved", StationResponse.from(station));
	}

	@GetMapping
	@RequiresPermission(resource = "station", action = "read")
	ApiResponse<List<StationResponse>> list(@RequestParam(required = false) String keyword,
			@RequestParam(required = false) Long cursor, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size) {
		if (cursor != null) {
			CursorPageResult<Station> stations = listStationsUseCase.executeAfterCursor(keyword, cursor, size);
			return ApiResponse.ok("Stations retrieved", stations.map(StationResponse::from).content(),
					CursorMeta.from(stations, size));
		}

		PageResult<Station> stations = listStationsUseCase.execute(keyword, page, size);
		return ApiResponse.ok("Stations retrieved", stations.map(StationResponse::from).content(),
				PageMeta.from(stations));
	}

	@PutMapping("/{id}")
	@RequiresPermission(resource = "station", action = "update")
	ApiResponse<StationResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateStationRequest request) {
		Station station = updateStationUseCase.execute(id, request.name(), request.address(), request.phone(),
				request.latitude(), request.longitude(), request.enabled());
		return ApiResponse.ok("Station updated", StationResponse.from(station));
	}

	@DeleteMapping("/{id}")
	@RequiresPermission(resource = "station", action = "delete")
	ApiResponse<Void> delete(@PathVariable Long id) {
		deleteStationUseCase.execute(id);
		return ApiResponse.ok("Station deleted", null);
	}

}
