package com.seanglay.fuelstation.product.application;

import org.springframework.transaction.annotation.Transactional;

import com.seanglay.fuelstation.product.domain.Product;
import com.seanglay.fuelstation.product.domain.ProductRepository;
import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.CursorPageResult;
import com.seanglay.fuelstation.shared.domain.PageResult;

@UseCase
public class ListProductsUseCase {

	private static final int MAX_PAGE_SIZE = 100;

	private static final int DEFAULT_PAGE_SIZE = 20;

	private final ProductRepository productRepository;

	public ListProductsUseCase(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional(readOnly = true)
	public PageResult<Product> execute(String keyword, int page, int size) {
		int safePage = Math.max(page, 0);
		int safeSize = normalizeSize(size);

		return productRepository.search(keyword, safePage, safeSize);
	}

	@Transactional(readOnly = true)
	public CursorPageResult<Product> executeAfterCursor(String keyword, Long cursor, int size) {
		int safeSize = normalizeSize(size);

		return productRepository.searchAfter(keyword, cursor, safeSize);
	}

	private static int normalizeSize(int size) {
		return size < 1 ? DEFAULT_PAGE_SIZE : Math.min(size, MAX_PAGE_SIZE);
	}

}
