package com.seanglay.fuelstation.product.domain;

import java.util.Optional;

import com.seanglay.fuelstation.shared.domain.CursorPageResult;
import com.seanglay.fuelstation.shared.domain.PageResult;

public interface ProductRepository {

	Optional<Product> findById(Long id);

	PageResult<Product> search(String keyword, int page, int size);

	CursorPageResult<Product> searchAfter(String keyword, Long cursor, int size);

	boolean existsBySku(String sku);

	Product save(Product product);

	void deleteById(Long id);

}
