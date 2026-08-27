package com.seanglay.fuelstation.product.application;

import org.springframework.transaction.annotation.Transactional;

import com.seanglay.fuelstation.product.domain.Product;
import com.seanglay.fuelstation.product.domain.ProductRepository;
import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.NotFoundException;

@UseCase
public class GetProductUseCase {

	private final ProductRepository productRepository;

	public GetProductUseCase(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional(readOnly = true)
	public Product execute(Long id) {
		return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found: " + id));
	}

}
