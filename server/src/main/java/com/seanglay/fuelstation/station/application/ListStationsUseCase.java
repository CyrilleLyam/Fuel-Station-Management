package com.seanglay.fuelstation.station.application;

import org.springframework.transaction.annotation.Transactional;

import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.CursorPageResult;
import com.seanglay.fuelstation.shared.domain.PageResult;
import com.seanglay.fuelstation.station.domain.Station;
import com.seanglay.fuelstation.station.domain.StationRepository;

@UseCase
public class ListStationsUseCase {

	private static final int MAX_PAGE_SIZE = 100;

	private static final int DEFAULT_PAGE_SIZE = 20;

	private final StationRepository stationRepository;

	public ListStationsUseCase(StationRepository stationRepository) {
		this.stationRepository = stationRepository;
	}

	@Transactional(readOnly = true)
	public PageResult<Station> execute(String keyword, int page, int size) {
		int safePage = Math.max(page, 0);
		int safeSize = normalizeSize(size);

		return stationRepository.search(keyword, safePage, safeSize);
	}

	@Transactional(readOnly = true)
	public CursorPageResult<Station> executeAfterCursor(String keyword, Long cursor, int size) {
		int safeSize = normalizeSize(size);

		return stationRepository.searchAfter(keyword, cursor, safeSize);
	}

	private static int normalizeSize(int size) {
		return size < 1 ? DEFAULT_PAGE_SIZE : Math.min(size, MAX_PAGE_SIZE);
	}

}
