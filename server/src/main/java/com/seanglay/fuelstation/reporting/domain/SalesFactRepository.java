package com.seanglay.fuelstation.reporting.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SalesFactRepository {

	boolean existsBySaleReference(UUID saleReference);

	List<DailySalesRow> dailySales(Long stationId, LocalDate from, LocalDate to);

	List<ProductSalesRow> salesByProduct(Long stationId, LocalDate from, LocalDate to);

	List<AttendantSalesRow> salesByAttendant(Long stationId, LocalDate from, LocalDate to);

	SalesFact save(SalesFact fact);

}
