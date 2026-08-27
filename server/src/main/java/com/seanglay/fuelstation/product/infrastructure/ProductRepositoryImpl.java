package com.seanglay.fuelstation.product.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.seanglay.fuelstation.product.domain.Product;
import com.seanglay.fuelstation.product.domain.ProductRepository;
import com.seanglay.fuelstation.shared.domain.CursorPageResult;
import com.seanglay.fuelstation.shared.domain.PageResult;

@Repository
class ProductRepositoryImpl implements ProductRepository {

	private final ProductJpaRepository jpaRepository;

	ProductRepositoryImpl(ProductJpaRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	@Override
	public Optional<Product> findById(Long id) {
		return jpaRepository.findById(id);
	}

	@Override
	public PageResult<Product> search(String keyword, int page, int size) {
		PageRequest pageRequest = PageRequest.of(page, size);
		String term = keyword == null ? "" : keyword;
		Page<Product> result = jpaRepository.findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(term, term,
				pageRequest);
		return new PageResult<>(result.getContent(), result.getNumber(), result.getSize(), result.getTotalElements(),
				result.getTotalPages());
	}

	@Override
	public CursorPageResult<Product> searchAfter(String keyword, Long cursor, int size) {
		String term = keyword == null ? "" : keyword;
		List<Product> rows = jpaRepository.searchAfter(term, cursor, PageRequest.of(0, size + 1));

		boolean hasNext = rows.size() > size;
		List<Product> content = hasNext ? rows.subList(0, size) : rows;
		Long nextCursor = hasNext ? content.get(content.size() - 1).getId() : null;

		return new CursorPageResult<>(content, nextCursor, hasNext);
	}

	@Override
	public boolean existsBySku(String sku) {
		return jpaRepository.existsBySku(sku);
	}

	@Override
	public Product save(Product product) {
		return jpaRepository.save(product);
	}

	@Override
	public void deleteById(Long id) {
		jpaRepository.deleteById(id);
	}

}
