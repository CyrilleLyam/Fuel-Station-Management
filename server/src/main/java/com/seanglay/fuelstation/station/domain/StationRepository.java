package com.seanglay.fuelstation.station.domain;

import java.util.Optional;

import com.seanglay.fuelstation.shared.domain.CursorPageResult;
import com.seanglay.fuelstation.shared.domain.PageResult;

public interface StationRepository {

	Optional<Station> findById(Long id);

	PageResult<Station> search(String keyword, int page, int size);

	CursorPageResult<Station> searchAfter(String keyword, Long cursor, int size);

	boolean existsByCode(String code);

	Station save(Station station);

	void deleteById(Long id);

}
