package com.seanglay.fuelstation.product.application;

import com.seanglay.fuelstation.product.domain.ProductRepository;
import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.NotFoundException;

@UseCase
public class DeleteProductUseCase {

	private final ProductRepository productRepository;

	public DeleteProductUseCase(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public void execute(Long id) {
		if (!productRepository.findById(id).isPresent()) {
			throw new NotFoundException("Product not found: " + id);
		}

		productRepository.deleteById(id);
	}

}
