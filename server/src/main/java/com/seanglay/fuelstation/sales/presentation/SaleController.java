package com.seanglay.fuelstation.sales.presentation;

import java.time.Instant;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seanglay.fuelstation.iam.RequiresPermission;
import com.seanglay.fuelstation.sales.application.GetSaleUseCase;
import com.seanglay.fuelstation.sales.application.ListSalesUseCase;
import com.seanglay.fuelstation.sales.application.RecordSaleUseCase;
import com.seanglay.fuelstation.sales.domain.Sale;
import com.seanglay.fuelstation.sales.presentation.dto.RecordSaleRequest;
import com.seanglay.fuelstation.sales.presentation.dto.SaleResponse;
import com.seanglay.fuelstation.shared.domain.CursorPageResult;
import com.seanglay.fuelstation.shared.domain.PageResult;
import com.seanglay.fuelstation.shared.presentation.ApiResponse;
import com.seanglay.fuelstation.shared.presentation.CursorMeta;
import com.seanglay.fuelstation.shared.presentation.PageMeta;

@RestController
@RequestMapping("/sales")
class SaleController {

	private final RecordSaleUseCase recordSaleUseCase;

	private final GetSaleUseCase getSaleUseCase;

	private final ListSalesUseCase listSalesUseCase;

	SaleController(RecordSaleUseCase recordSaleUseCase, GetSaleUseCase getSaleUseCase,
			ListSalesUseCase listSalesUseCase) {
		this.recordSaleUseCase = recordSaleUseCase;
		this.getSaleUseCase = getSaleUseCase;
		this.listSalesUseCase = listSalesUseCase;
	}

	@PostMapping
	@RequiresPermission(resource = "sale", action = "create")
	ResponseEntity<ApiResponse<SaleResponse>> record(@Valid @RequestBody RecordSaleRequest request,
			Authentication authentication) {
		Sale sale = recordSaleUseCase.execute(request.stationId(), request.tankId(), request.productId(),
				authentication.getName(), request.quantity(), request.paymentMethod(), request.soldAt());
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Sale recorded", SaleResponse.from(sale)));
	}

	@GetMapping("/{id}")
	@RequiresPermission(resource = "sale", action = "read")
	ApiResponse<SaleResponse> get(@PathVariable Long id) {
		Sale sale = getSaleUseCase.execute(id);
		return ApiResponse.ok("Sale retrieved", SaleResponse.from(sale));
	}

	@GetMapping
	@RequiresPermission(resource = "sale", action = "read")
	ApiResponse<List<SaleResponse>> list(@RequestParam(required = false) Long stationId,
			@RequestParam(required = false) Long productId,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
			@RequestParam(required = false) Long cursor, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size) {
		if (cursor != null) {
			CursorPageResult<Sale> sales = listSalesUseCase.executeAfterCursor(stationId, productId, from, to, cursor,
					size);
			return ApiResponse.ok("Sales retrieved", sales.map(SaleResponse::from).content(),
					CursorMeta.from(sales, size));
		}

		PageResult<Sale> sales = listSalesUseCase.execute(stationId, productId, from, to, page, size);
		return ApiResponse.ok("Sales retrieved", sales.map(SaleResponse::from).content(), PageMeta.from(sales));
	}

}
