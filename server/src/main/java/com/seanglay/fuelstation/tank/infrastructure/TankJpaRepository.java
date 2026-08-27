package com.seanglay.fuelstation.tank.infrastructure;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.seanglay.fuelstation.tank.domain.Tank;

interface TankJpaRepository extends JpaRepository<Tank, Long> {

	@Query("""
			select t from Tank t
			where (:stationId is null or t.stationId = :stationId)
			and lower(t.label) like lower(concat('%', :keyword, '%'))
			order by t.id asc
			""")
	Page<Tank> search(@Param("stationId") Long stationId, @Param("keyword") String keyword, Pageable pageable);

	@Query("""
			select t from Tank t
			where (:stationId is null or t.stationId = :stationId)
			and (:cursor is null or t.id > :cursor)
			and lower(t.label) like lower(concat('%', :keyword, '%'))
			order by t.id asc
			""")
	List<Tank> searchAfter(@Param("stationId") Long stationId, @Param("keyword") String keyword,
			@Param("cursor") Long cursor, Pageable pageable);

}
