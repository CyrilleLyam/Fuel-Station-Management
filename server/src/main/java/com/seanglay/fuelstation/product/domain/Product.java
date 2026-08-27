package com.seanglay.fuelstation.product.domain;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.seanglay.fuelstation.shared.domain.AggregateRoot;

@Entity
@Table(name = "products")
public class Product extends AggregateRoot<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "sku", nullable = false, unique = true)
	private String sku;

	@Enumerated(EnumType.STRING)
	@Column(name = "fuel_type", nullable = false)
	private FuelType fuelType;

	@Column(name = "unit", nullable = false)
	private String unit;

	@Column(name = "unit_price", nullable = false)
	private BigDecimal unitPrice;

	@Column(name = "active", nullable = false)
	private boolean active;

	protected Product() {
	}

	public Product(String name, String sku, FuelType fuelType, String unit, BigDecimal unitPrice) {
		this.name = name;
		this.sku = sku;
		this.fuelType = fuelType;
		this.unit = unit;
		this.unitPrice = requireNonNegative(unitPrice);
		this.active = true;
	}

	@Override
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSku() {
		return sku;
	}

	public FuelType getFuelType() {
		return fuelType;
	}

	public String getUnit() {
		return unit;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public boolean isActive() {
		return active;
	}

	public void update(String name, FuelType fuelType, String unit) {
		this.name = name;
		this.fuelType = fuelType;
		this.unit = unit;
	}

	public void changePrice(BigDecimal unitPrice) {
		this.unitPrice = requireNonNegative(unitPrice);
	}

	public void activate() {
		this.active = true;
	}

	public void deactivate() {
		this.active = false;
	}

	private static BigDecimal requireNonNegative(BigDecimal unitPrice) {
		if (unitPrice == null || unitPrice.signum() < 0) {
			throw new IllegalArgumentException("Unit price must be zero or greater");
		}
		return unitPrice;
	}

}
