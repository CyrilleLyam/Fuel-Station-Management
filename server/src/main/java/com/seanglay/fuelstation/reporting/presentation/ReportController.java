package com.seanglay.fuelstation.reporting.presentation;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seanglay.fuelstation.iam.RequiresPermission;
import com.seanglay.fuelstation.reporting.application.GetSalesReportUseCase;
import com.seanglay.fuelstation.reporting.presentation.dto.AttendantSalesResponse;
import com.seanglay.fuelstation.reporting.presentation.dto.DailySalesResponse;
import com.seanglay.fuelstation.reporting.presentation.dto.ProductSalesResponse;
import com.seanglay.fuelstation.shared.presentation.ApiResponse;

@RestController
@RequestMapping("/reports")
class ReportController {

	private final GetSalesReportUseCase getSalesReportUseCase;

	ReportController(GetSalesReportUseCase getSalesReportUseCase) {
		this.getSalesReportUseCase = getSalesReportUseCase;
	}

	@GetMapping("/sales/daily")
	@RequiresPermission(resource = "report", action = "read")
	ApiResponse<List<DailySalesResponse>> daily(@RequestParam(required = false) Long stationId,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
		List<DailySalesResponse> rows = getSalesReportUseCase.daily(stationId, from, to)
			.stream()
			.map(DailySalesResponse::from)
			.toList();
		return ApiResponse.ok("Daily sales retrieved", rows);
	}

	@GetMapping("/sales/products")
	@RequiresPermission(resource = "report", action = "read")
	ApiResponse<List<ProductSalesResponse>> byProduct(@RequestParam(required = false) Long stationId,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
		List<ProductSalesResponse> rows = getSalesReportUseCase.byProduct(stationId, from, to)
			.stream()
			.map(ProductSalesResponse::from)
			.toList();
		return ApiResponse.ok("Product sales retrieved", rows);
	}

	@GetMapping("/sales/attendants")
	@RequiresPermission(resource = "report", action = "read")
	ApiResponse<List<AttendantSalesResponse>> byAttendant(@RequestParam(required = false) Long stationId,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
		List<AttendantSalesResponse> rows = getSalesReportUseCase.byAttendant(stationId, from, to)
			.stream()
			.map(AttendantSalesResponse::from)
			.toList();
		return ApiResponse.ok("Attendant sales retrieved", rows);
	}

}
