package com.seanglay.fuelstation.station.infrastructure;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.seanglay.fuelstation.station.domain.Station;

interface StationJpaRepository extends JpaRepository<Station, Long> {

	boolean existsByCode(String code);

	Page<Station> findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(String name, String code,
			Pageable pageable);

	@Query("""
			select s from Station s
			where (:cursor is null or s.id > :cursor)
			and (lower(s.name) like lower(concat('%', :keyword, '%'))
				or lower(s.code) like lower(concat('%', :keyword, '%')))
			order by s.id asc
			""")
	List<Station> searchAfter(@Param("keyword") String keyword, @Param("cursor") Long cursor, Pageable pageable);

}
