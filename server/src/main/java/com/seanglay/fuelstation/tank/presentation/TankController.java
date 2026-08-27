package com.seanglay.fuelstation.tank.presentation;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
import com.seanglay.fuelstation.tank.application.AssignProductUseCase;
import com.seanglay.fuelstation.tank.application.CreateTankUseCase;
import com.seanglay.fuelstation.tank.application.DeleteTankUseCase;
import com.seanglay.fuelstation.tank.application.GetTankUseCase;
import com.seanglay.fuelstation.tank.application.ListTanksUseCase;
import com.seanglay.fuelstation.tank.application.RecordTankMovementUseCase;
import com.seanglay.fuelstation.tank.application.SetTankCapacityUseCase;
import com.seanglay.fuelstation.tank.application.UpdateTankUseCase;
import com.seanglay.fuelstation.tank.domain.Tank;
import com.seanglay.fuelstation.tank.presentation.dto.AssignProductRequest;
import com.seanglay.fuelstation.tank.presentation.dto.CreateTankRequest;
import com.seanglay.fuelstation.tank.presentation.dto.SetCapacityRequest;
import com.seanglay.fuelstation.tank.presentation.dto.TankMovementRequest;
import com.seanglay.fuelstation.tank.presentation.dto.TankResponse;
import com.seanglay.fuelstation.tank.presentation.dto.UpdateTankRequest;

@RestController
@RequestMapping("/tanks")
class TankController {

	private final CreateTankUseCase createTankUseCase;

	private final GetTankUseCase getTankUseCase;

	private final ListTanksUseCase listTanksUseCase;

	private final UpdateTankUseCase updateTankUseCase;

	private final AssignProductUseCase assignProductUseCase;

	private final SetTankCapacityUseCase setTankCapacityUseCase;

	private final RecordTankMovementUseCase recordTankMovementUseCase;

	private final DeleteTankUseCase deleteTankUseCase;

	TankController(CreateTankUseCase createTankUseCase, GetTankUseCase getTankUseCase,
			ListTanksUseCase listTanksUseCase, UpdateTankUseCase updateTankUseCase,
			AssignProductUseCase assignProductUseCase, SetTankCapacityUseCase setTankCapacityUseCase,
			RecordTankMovementUseCase recordTankMovementUseCase, DeleteTankUseCase deleteTankUseCase) {
		this.createTankUseCase = createTankUseCase;
		this.getTankUseCase = getTankUseCase;
		this.listTanksUseCase = listTanksUseCase;
		this.updateTankUseCase = updateTankUseCase;
		this.assignProductUseCase = assignProductUseCase;
		this.setTankCapacityUseCase = setTankCapacityUseCase;
		this.recordTankMovementUseCase = recordTankMovementUseCase;
		this.deleteTankUseCase = deleteTankUseCase;
	}

	@PostMapping
	@RequiresPermission(resource = "tank", action = "create")
	ResponseEntity<ApiResponse<TankResponse>> create(@Valid @RequestBody CreateTankRequest request) {
		Tank tank = createTankUseCase.execute(request.stationId(), request.label(), request.capacity(),
				request.productId());
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Tank created", TankResponse.from(tank)));
	}

	@GetMapping("/{id}")
	@RequiresPermission(resource = "tank", action = "read")
	ApiResponse<TankResponse> get(@PathVariable Long id) {
		Tank tank = getTankUseCase.execute(id);
		return ApiResponse.ok("Tank retrieved", TankResponse.from(tank));
	}

	@GetMapping
	@RequiresPermission(resource = "tank", action = "read")
	ApiResponse<List<TankResponse>> list(@RequestParam(required = false) Long stationId,
			@RequestParam(required = false) String keyword, @RequestParam(required = false) Long cursor,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
		if (cursor != null) {
			CursorPageResult<Tank> tanks = listTanksUseCase.executeAfterCursor(stationId, keyword, cursor, size);
			return ApiResponse.ok("Tanks retrieved", tanks.map(TankResponse::from).content(),
					CursorMeta.from(tanks, size));
		}

		PageResult<Tank> tanks = listTanksUseCase.execute(stationId, keyword, page, size);
		return ApiResponse.ok("Tanks retrieved", tanks.map(TankResponse::from).content(), PageMeta.from(tanks));
	}

	@PutMapping("/{id}")
	@RequiresPermission(resource = "tank", action = "update")
	ApiResponse<TankResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateTankRequest request) {
		Tank tank = updateTankUseCase.execute(id, request.label(), request.active());
		return ApiResponse.ok("Tank updated", TankResponse.from(tank));
	}

	@PatchMapping("/{id}/product")
	@RequiresPermission(resource = "tank", action = "update")
	ApiResponse<TankResponse> assignProduct(@PathVariable Long id, @Valid @RequestBody AssignProductRequest request) {
		Tank tank = assignProductUseCase.execute(id, request.productId());
		return ApiResponse.ok("Product assigned", TankResponse.from(tank));
	}

	@PatchMapping("/{id}/capacity")
	@RequiresPermission(resource = "tank", action = "update")
	ApiResponse<TankResponse> setCapacity(@PathVariable Long id, @Valid @RequestBody SetCapacityRequest request) {
		Tank tank = setTankCapacityUseCase.execute(id, request.capacity());
		return ApiResponse.ok("Tank capacity updated", TankResponse.from(tank));
	}

	@PostMapping("/{id}/deliveries")
	@RequiresPermission(resource = "tank", action = "update")
	ApiResponse<TankResponse> recordDelivery(@PathVariable Long id, @Valid @RequestBody TankMovementRequest request) {
		Tank tank = recordTankMovementUseCase.recordDelivery(id, request.amount());
		return ApiResponse.ok("Delivery recorded", TankResponse.from(tank));
	}

	@PostMapping("/{id}/dispenses")
	@RequiresPermission(resource = "tank", action = "update")
	ApiResponse<TankResponse> recordDispense(@PathVariable Long id, @Valid @RequestBody TankMovementRequest request) {
		Tank tank = recordTankMovementUseCase.recordDispense(id, request.amount());
		return ApiResponse.ok("Dispense recorded", TankResponse.from(tank));
	}

	@DeleteMapping("/{id}")
	@RequiresPermission(resource = "tank", action = "delete")
	ApiResponse<Void> delete(@PathVariable Long id) {
		deleteTankUseCase.execute(id);
		return ApiResponse.ok("Tank deleted", null);
	}

}
