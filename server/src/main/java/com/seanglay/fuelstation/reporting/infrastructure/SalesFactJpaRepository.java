package com.seanglay.fuelstation.reporting.infrastructure;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.seanglay.fuelstation.reporting.domain.AttendantSalesRow;
import com.seanglay.fuelstation.reporting.domain.DailySalesRow;
import com.seanglay.fuelstation.reporting.domain.ProductSalesRow;
import com.seanglay.fuelstation.reporting.domain.SalesFact;

interface SalesFactJpaRepository extends JpaRepository<SalesFact, Long> {

	boolean existsBySaleReference(UUID saleReference);

	@Query("""
			select new com.seanglay.fuelstation.reporting.domain.DailySalesRow(
				f.businessDate, sum(f.quantity), sum(f.totalAmount), count(f))
			from SalesFact f
			where (:stationId is null or f.stationId = :stationId)
			and f.businessDate between :from and :to
			group by f.businessDate
			order by f.businessDate asc
			""")
	List<DailySalesRow> dailySales(@Param("stationId") Long stationId, @Param("from") LocalDate from,
			@Param("to") LocalDate to);

	@Query("""
			select new com.seanglay.fuelstation.reporting.domain.ProductSalesRow(
				f.productId, sum(f.quantity), sum(f.totalAmount), count(f))
			from SalesFact f
			where (:stationId is null or f.stationId = :stationId)
			and f.businessDate between :from and :to
			group by f.productId
			order by sum(f.totalAmount) desc
			""")
	List<ProductSalesRow> salesByProduct(@Param("stationId") Long stationId, @Param("from") LocalDate from,
			@Param("to") LocalDate to);

	@Query("""
			select new com.seanglay.fuelstation.reporting.domain.AttendantSalesRow(
				f.attendant, sum(f.quantity), sum(f.totalAmount), count(f))
			from SalesFact f
			where (:stationId is null or f.stationId = :stationId)
			and f.businessDate between :from and :to
			group by f.attendant
			order by sum(f.totalAmount) desc
			""")
	List<AttendantSalesRow> salesByAttendant(@Param("stationId") Long stationId, @Param("from") LocalDate from,
			@Param("to") LocalDate to);

}
