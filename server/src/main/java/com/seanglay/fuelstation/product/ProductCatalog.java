package com.seanglay.fuelstation.product;

import java.util.Optional;

public interface ProductCatalog {

	Optional<ProductSnapshot> findById(Long id);

}
