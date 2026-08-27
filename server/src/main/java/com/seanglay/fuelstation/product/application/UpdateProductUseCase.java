package com.seanglay.fuelstation.product.application;

import com.seanglay.fuelstation.product.domain.FuelType;
import com.seanglay.fuelstation.product.domain.Product;
import com.seanglay.fuelstation.product.domain.ProductRepository;
import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.NotFoundException;

@UseCase
public class UpdateProductUseCase {

	private final ProductRepository productRepository;

	public UpdateProductUseCase(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Product execute(Long id, String name, FuelType fuelType, String unit, boolean active) {
		Product product = productRepository.findById(id)
			.orElseThrow(() -> new NotFoundException("Product not found: " + id));

		product.update(name, fuelType, unit);

		if (active) {
			product.activate();
		}
		else {
			product.deactivate();
		}

		return productRepository.save(product);
	}

}
