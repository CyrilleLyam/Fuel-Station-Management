package com.seanglay.fuelstation.reporting.infrastructure;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.seanglay.fuelstation.reporting.domain.AttendantSalesRow;
import com.seanglay.fuelstation.reporting.domain.DailySalesRow;
import com.seanglay.fuelstation.reporting.domain.ProductSalesRow;
import com.seanglay.fuelstation.reporting.domain.SalesFact;
import com.seanglay.fuelstation.reporting.domain.SalesFactRepository;

@Repository
class SalesFactRepositoryImpl implements SalesFactRepository {

	private final SalesFactJpaRepository jpaRepository;

	SalesFactRepositoryImpl(SalesFactJpaRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	@Override
	public boolean existsBySaleReference(UUID saleReference) {
		return jpaRepository.existsBySaleReference(saleReference);
	}

	@Override
	public List<DailySalesRow> dailySales(Long stationId, LocalDate from, LocalDate to) {
		return jpaRepository.dailySales(stationId, from, to);
	}

	@Override
	public List<ProductSalesRow> salesByProduct(Long stationId, LocalDate from, LocalDate to) {
		return jpaRepository.salesByProduct(stationId, from, to);
	}

	@Override
	public List<AttendantSalesRow> salesByAttendant(Long stationId, LocalDate from, LocalDate to) {
		return jpaRepository.salesByAttendant(stationId, from, to);
	}

	@Override
	public SalesFact save(SalesFact fact) {
		return jpaRepository.save(fact);
	}

}
