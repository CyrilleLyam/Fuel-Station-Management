package com.seanglay.fuelstation.sales.infrastructure;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.seanglay.fuelstation.sales.domain.Sale;

interface SaleJpaRepository extends JpaRepository<Sale, Long> {

	@Query("""
			select s from Sale s
			where (:stationId is null or s.stationId = :stationId)
			and (:productId is null or s.productId = :productId)
			and s.soldAt between :from and :to
			order by s.id asc
			""")
	Page<Sale> search(@Param("stationId") Long stationId, @Param("productId") Long productId,
			@Param("from") Instant from, @Param("to") Instant to, Pageable pageable);

	@Query("""
			select s from Sale s
			where (:stationId is null or s.stationId = :stationId)
			and (:productId is null or s.productId = :productId)
			and (:cursor is null or s.id > :cursor)
			and s.soldAt between :from and :to
			order by s.id asc
			""")
	List<Sale> searchAfter(@Param("stationId") Long stationId, @Param("productId") Long productId,
			@Param("from") Instant from, @Param("to") Instant to, @Param("cursor") Long cursor, Pageable pageable);

}
