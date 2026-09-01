package com.seanglay.fuelstation.product.infrastructure;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.seanglay.fuelstation.product.ProductCatalog;
import com.seanglay.fuelstation.product.ProductSnapshot;
import com.seanglay.fuelstation.product.domain.Product;
import com.seanglay.fuelstation.product.domain.ProductRepository;

@Component
class ProductCatalogAdapter implements ProductCatalog {

	private final ProductRepository productRepository;

	ProductCatalogAdapter(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ProductSnapshot> findById(Long id) {
		return productRepository.findById(id).map(ProductCatalogAdapter::toSnapshot);
	}

	private static ProductSnapshot toSnapshot(Product product) {
		return new ProductSnapshot(product.getId(), product.getName(), product.getSku(), product.getUnit(),
				product.getUnitPrice(), product.isActive());
	}

}
