package com.seanglay.fuelstation.sales.application;

import java.time.Instant;

import org.springframework.transaction.annotation.Transactional;

import com.seanglay.fuelstation.sales.domain.Sale;
import com.seanglay.fuelstation.sales.domain.SaleRepository;
import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.CursorPageResult;
import com.seanglay.fuelstation.shared.domain.PageResult;

@UseCase
public class ListSalesUseCase {

	private static final int MAX_PAGE_SIZE = 100;

	private static final int DEFAULT_PAGE_SIZE = 20;

	private final SaleRepository saleRepository;

	public ListSalesUseCase(SaleRepository saleRepository) {
		this.saleRepository = saleRepository;
	}

	@Transactional(readOnly = true)
	public PageResult<Sale> execute(Long stationId, Long productId, Instant from, Instant to, int page, int size) {
		int safePage = Math.max(page, 0);
		int safeSize = normalizeSize(size);

		return saleRepository.search(stationId, productId, from, to, safePage, safeSize);
	}

	@Transactional(readOnly = true)
	public CursorPageResult<Sale> executeAfterCursor(Long stationId, Long productId, Instant from, Instant to,
			Long cursor, int size) {
		int safeSize = normalizeSize(size);

		return saleRepository.searchAfter(stationId, productId, from, to, cursor, safeSize);
	}

	private static int normalizeSize(int size) {
		return size < 1 ? DEFAULT_PAGE_SIZE : Math.min(size, MAX_PAGE_SIZE);
	}

}
