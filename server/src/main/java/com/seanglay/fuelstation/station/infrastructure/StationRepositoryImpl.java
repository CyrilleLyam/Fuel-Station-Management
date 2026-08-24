package com.seanglay.fuelstation.station.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.seanglay.fuelstation.shared.domain.CursorPageResult;
import com.seanglay.fuelstation.shared.domain.PageResult;
import com.seanglay.fuelstation.station.domain.Station;
import com.seanglay.fuelstation.station.domain.StationRepository;

@Repository
class StationRepositoryImpl implements StationRepository {

	private final StationJpaRepository jpaRepository;

	StationRepositoryImpl(StationJpaRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	@Override
	public Optional<Station> findById(Long id) {
		return jpaRepository.findById(id);
	}

	@Override
	public PageResult<Station> search(String keyword, int page, int size) {
		PageRequest pageRequest = PageRequest.of(page, size);
		String term = keyword == null ? "" : keyword;
		Page<Station> result = jpaRepository.findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(term, term,
				pageRequest);
		return new PageResult<>(result.getContent(), result.getNumber(), result.getSize(), result.getTotalElements(),
				result.getTotalPages());
	}

	@Override
	public CursorPageResult<Station> searchAfter(String keyword, Long cursor, int size) {
		String term = keyword == null ? "" : keyword;
		List<Station> rows = jpaRepository.searchAfter(term, cursor, PageRequest.of(0, size + 1));

		boolean hasNext = rows.size() > size;
		List<Station> content = hasNext ? rows.subList(0, size) : rows;
		Long nextCursor = hasNext ? content.get(content.size() - 1).getId() : null;

		return new CursorPageResult<>(content, nextCursor, hasNext);
	}

	@Override
	public boolean existsByCode(String code) {
		return jpaRepository.existsByCode(code);
	}

	@Override
	public Station save(Station station) {
		return jpaRepository.save(station);
	}

	@Override
	public void deleteById(Long id) {
		jpaRepository.deleteById(id);
	}

}
