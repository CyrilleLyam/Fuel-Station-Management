package com.seanglay.fuelstation.sales.domain;

import java.time.Instant;
import java.util.Optional;

import com.seanglay.fuelstation.shared.domain.CursorPageResult;
import com.seanglay.fuelstation.shared.domain.PageResult;

public interface SaleRepository {

	Optional<Sale> findById(Long id);

	PageResult<Sale> search(Long stationId, Long productId, Instant from, Instant to, int page, int size);

	CursorPageResult<Sale> searchAfter(Long stationId, Long productId, Instant from, Instant to, Long cursor, int size);

	Sale save(Sale sale);

}
