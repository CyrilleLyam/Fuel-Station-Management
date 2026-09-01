package com.seanglay.fuelstation.tank.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.seanglay.fuelstation.shared.domain.CursorPageResult;
import com.seanglay.fuelstation.shared.domain.PageResult;
import com.seanglay.fuelstation.tank.domain.Tank;
import com.seanglay.fuelstation.tank.domain.TankRepository;

@Repository
class TankRepositoryImpl implements TankRepository {

	private final TankJpaRepository jpaRepository;

	TankRepositoryImpl(TankJpaRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	@Override
	public Optional<Tank> findById(Long id) {
		return jpaRepository.findById(id);
	}

	@Override
	public Optional<Tank> findByIdForUpdate(Long id) {
		return jpaRepository.findByIdForUpdate(id);
	}

	@Override
	public PageResult<Tank> search(Long stationId, String keyword, int page, int size) {
		String term = keyword == null ? "" : keyword;
		Page<Tank> result = jpaRepository.search(stationId, term, PageRequest.of(page, size));
		return new PageResult<>(result.getContent(), result.getNumber(), result.getSize(), result.getTotalElements(),
				result.getTotalPages());
	}

	@Override
	public CursorPageResult<Tank> searchAfter(Long stationId, String keyword, Long cursor, int size) {
		String term = keyword == null ? "" : keyword;
		List<Tank> rows = jpaRepository.searchAfter(stationId, term, cursor, PageRequest.of(0, size + 1));

		boolean hasNext = rows.size() > size;
		List<Tank> content = hasNext ? rows.subList(0, size) : rows;
		Long nextCursor = hasNext ? content.get(content.size() - 1).getId() : null;

		return new CursorPageResult<>(content, nextCursor, hasNext);
	}

	@Override
	public Tank save(Tank tank) {
		return jpaRepository.save(tank);
	}

	@Override
	public void deleteById(Long id) {
		jpaRepository.deleteById(id);
	}

}
