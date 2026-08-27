package com.seanglay.fuelstation.product.application;

import java.math.BigDecimal;

import com.seanglay.fuelstation.product.domain.Product;
import com.seanglay.fuelstation.product.domain.ProductRepository;
import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.NotFoundException;

@UseCase
public class ChangeProductPriceUseCase {

	private final ProductRepository productRepository;

	public ChangeProductPriceUseCase(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Product execute(Long id, BigDecimal unitPrice) {
		Product product = productRepository.findById(id)
			.orElseThrow(() -> new NotFoundException("Product not found: " + id));

		product.changePrice(unitPrice);

		return productRepository.save(product);
	}

}
