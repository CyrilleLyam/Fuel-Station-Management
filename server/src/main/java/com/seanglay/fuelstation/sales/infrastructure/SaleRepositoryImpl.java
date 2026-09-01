package com.seanglay.fuelstation.sales.infrastructure;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.seanglay.fuelstation.sales.domain.Sale;
import com.seanglay.fuelstation.sales.domain.SaleRepository;
import com.seanglay.fuelstation.shared.domain.CursorPageResult;
import com.seanglay.fuelstation.shared.domain.PageResult;

@Repository
class SaleRepositoryImpl implements SaleRepository {

	private static final Instant OPEN_START = Instant.EPOCH;

	private static final Instant OPEN_END = Instant.parse("9999-12-31T23:59:59Z");

	private final SaleJpaRepository jpaRepository;

	SaleRepositoryImpl(SaleJpaRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	@Override
	public Optional<Sale> findById(Long id) {
		return jpaRepository.findById(id);
	}

	@Override
	public PageResult<Sale> search(Long stationId, Long productId, Instant from, Instant to, int page, int size) {
		Page<Sale> result = jpaRepository.search(stationId, productId, start(from), end(to),
				PageRequest.of(page, size));
		return new PageResult<>(result.getContent(), result.getNumber(), result.getSize(), result.getTotalElements(),
				result.getTotalPages());
	}

	@Override
	public CursorPageResult<Sale> searchAfter(Long stationId, Long productId, Instant from, Instant to, Long cursor,
			int size) {
		List<Sale> rows = jpaRepository.searchAfter(stationId, productId, start(from), end(to), cursor,
				PageRequest.of(0, size + 1));

		boolean hasNext = rows.size() > size;
		List<Sale> content = hasNext ? rows.subList(0, size) : rows;
		Long nextCursor = hasNext ? content.get(content.size() - 1).getId() : null;

		return new CursorPageResult<>(content, nextCursor, hasNext);
	}

	@Override
	public Sale save(Sale sale) {
		return jpaRepository.save(sale);
	}

	private static Instant start(Instant from) {
		return from == null ? OPEN_START : from;
	}

	private static Instant end(Instant to) {
		return to == null ? OPEN_END : to;
	}

}
