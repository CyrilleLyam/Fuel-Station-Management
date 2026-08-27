package com.seanglay.fuelstation.product.application;

import java.math.BigDecimal;

import com.seanglay.fuelstation.product.domain.FuelType;
import com.seanglay.fuelstation.product.domain.Product;
import com.seanglay.fuelstation.product.domain.ProductRepository;
import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.AlreadyExistsException;

@UseCase
public class CreateProductUseCase {

	private final ProductRepository productRepository;

	public CreateProductUseCase(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Product execute(String name, String sku, FuelType fuelType, String unit, BigDecimal unitPrice) {
		if (productRepository.existsBySku(sku)) {
			throw new AlreadyExistsException("Product SKU already taken: " + sku);
		}

		Product product = new Product(name, sku, fuelType, unit, unitPrice);
		return productRepository.save(product);
	}

}
