package com.seanglay.fuelstation.tank.domain;

import java.util.Optional;

import com.seanglay.fuelstation.shared.domain.CursorPageResult;
import com.seanglay.fuelstation.shared.domain.PageResult;

public interface TankRepository {

	Optional<Tank> findById(Long id);

	PageResult<Tank> search(Long stationId, String keyword, int page, int size);

	CursorPageResult<Tank> searchAfter(Long stationId, String keyword, Long cursor, int size);

	Tank save(Tank tank);

	void deleteById(Long id);

}
