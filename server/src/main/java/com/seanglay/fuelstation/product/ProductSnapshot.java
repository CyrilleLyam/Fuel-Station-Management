package com.seanglay.fuelstation.product;

import java.math.BigDecimal;

public record ProductSnapshot(Long id, String name, String sku, String unit, BigDecimal unitPrice, boolean active) {
}
