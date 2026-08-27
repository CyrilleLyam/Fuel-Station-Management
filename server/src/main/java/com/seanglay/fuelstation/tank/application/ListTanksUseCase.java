package com.seanglay.fuelstation.tank.application;

import org.springframework.transaction.annotation.Transactional;

import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.CursorPageResult;
import com.seanglay.fuelstation.shared.domain.PageResult;
import com.seanglay.fuelstation.tank.domain.Tank;
import com.seanglay.fuelstation.tank.domain.TankRepository;

@UseCase
public class ListTanksUseCase {

	private static final int MAX_PAGE_SIZE = 100;

	private static final int DEFAULT_PAGE_SIZE = 20;

	private final TankRepository tankRepository;

	public ListTanksUseCase(TankRepository tankRepository) {
		this.tankRepository = tankRepository;
	}

	@Transactional(readOnly = true)
	public PageResult<Tank> execute(Long stationId, String keyword, int page, int size) {
		int safePage = Math.max(page, 0);
		int safeSize = normalizeSize(size);

		return tankRepository.search(stationId, keyword, safePage, safeSize);
	}

	@Transactional(readOnly = true)
	public CursorPageResult<Tank> executeAfterCursor(Long stationId, String keyword, Long cursor, int size) {
		int safeSize = normalizeSize(size);

		return tankRepository.searchAfter(stationId, keyword, cursor, safeSize);
	}

	private static int normalizeSize(int size) {
		return size < 1 ? DEFAULT_PAGE_SIZE : Math.min(size, MAX_PAGE_SIZE);
	}

}
