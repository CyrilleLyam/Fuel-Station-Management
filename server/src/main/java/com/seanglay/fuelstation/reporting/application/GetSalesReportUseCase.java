package com.seanglay.fuelstation.reporting.application;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.seanglay.fuelstation.reporting.domain.AttendantSalesRow;
import com.seanglay.fuelstation.reporting.domain.DailySalesRow;
import com.seanglay.fuelstation.reporting.domain.ProductSalesRow;
import com.seanglay.fuelstation.reporting.domain.SalesFactRepository;
import com.seanglay.fuelstation.shared.application.UseCase;

@UseCase
public class GetSalesReportUseCase {

	private static final int DEFAULT_WINDOW_DAYS = 30;

	private final SalesFactRepository salesFactRepository;

	public GetSalesReportUseCase(SalesFactRepository salesFactRepository) {
		this.salesFactRepository = salesFactRepository;
	}

	@Transactional(readOnly = true)
	public List<DailySalesRow> daily(Long stationId, LocalDate from, LocalDate to) {
		LocalDate end = end(to);
		return salesFactRepository.dailySales(stationId, start(from, end), end);
	}

	@Transactional(readOnly = true)
	public List<ProductSalesRow> byProduct(Long stationId, LocalDate from, LocalDate to) {
		LocalDate end = end(to);
		return salesFactRepository.salesByProduct(stationId, start(from, end), end);
	}

	@Transactional(readOnly = true)
	public List<AttendantSalesRow> byAttendant(Long stationId, LocalDate from, LocalDate to) {
		LocalDate end = end(to);
		return salesFactRepository.salesByAttendant(stationId, start(from, end), end);
	}

	private static LocalDate end(LocalDate to) {
		return to == null ? LocalDate.now(ZoneOffset.UTC) : to;
	}

	private static LocalDate start(LocalDate from, LocalDate end) {
		return from == null ? end.minusDays(DEFAULT_WINDOW_DAYS) : from;
	}

}
