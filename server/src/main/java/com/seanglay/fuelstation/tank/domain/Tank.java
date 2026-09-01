package com.seanglay.fuelstation.tank.domain;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.seanglay.fuelstation.shared.domain.AggregateRoot;

@Entity
@Table(name = "tanks")
public class Tank extends AggregateRoot<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "station_id", nullable = false)
	private Long stationId;

	@Column(name = "product_id")
	private Long productId;

	@Column(name = "label", nullable = false)
	private String label;

	@Column(name = "capacity", nullable = false)
	private BigDecimal capacity;

	@Column(name = "current_quantity", nullable = false)
	private BigDecimal currentQuantity;

	@Column(name = "active", nullable = false)
	private boolean active;

	protected Tank() {
	}

	public Tank(Long stationId, String label, BigDecimal capacity) {
		this.stationId = stationId;
		this.label = label;
		this.capacity = requirePositive(capacity);
		this.currentQuantity = BigDecimal.ZERO;
		this.active = true;
	}

	@Override
	public Long getId() {
		return id;
	}

	public Long getStationId() {
		return stationId;
	}

	public Long getProductId() {
		return productId;
	}

	public String getLabel() {
		return label;
	}

	public BigDecimal getCapacity() {
		return capacity;
	}

	public BigDecimal getCurrentQuantity() {
		return currentQuantity;
	}

	public BigDecimal getAvailableSpace() {
		return capacity.subtract(currentQuantity);
	}

	public boolean isActive() {
		return active;
	}

	public void rename(String label) {
		this.label = label;
	}

	public void assignProduct(Long productId) {
		if (currentQuantity.signum() > 0) {
			throw new IllegalStateException("Cannot reassign product while tank still holds fuel");
		}
		this.productId = productId;
	}

	public void setCapacity(BigDecimal capacity) {
		BigDecimal next = requirePositive(capacity);
		if (next.compareTo(currentQuantity) < 0) {
			throw new IllegalArgumentException("Capacity cannot be below current quantity");
		}
		this.capacity = next;
	}

	public void recordDelivery(BigDecimal amount) {
		BigDecimal delivered = requirePositive(amount);
		if (productId == null) {
			throw new IllegalStateException("Assign a product before recording a delivery");
		}
		BigDecimal next = currentQuantity.add(delivered);
		if (next.compareTo(capacity) > 0) {
			throw new IllegalArgumentException("Delivery exceeds tank capacity");
		}
		this.currentQuantity = next;
	}

	public BigDecimal recordSale(BigDecimal amount) {
		BigDecimal sold = requirePositive(amount);

		if (sold.compareTo(currentQuantity) > 0) {
			BigDecimal shortfall = sold.subtract(currentQuantity);
			this.currentQuantity = BigDecimal.ZERO;
			return shortfall;
		}

		this.currentQuantity = currentQuantity.subtract(sold);
		return BigDecimal.ZERO;
	}

	public void recordDispense(BigDecimal amount) {
		BigDecimal dispensed = requirePositive(amount);
		BigDecimal next = currentQuantity.subtract(dispensed);
		if (next.signum() < 0) {
			throw new IllegalArgumentException("Dispense exceeds current quantity");
		}
		this.currentQuantity = next;
	}

	public void activate() {
		this.active = true;
	}

	public void deactivate() {
		this.active = false;
	}

	private static BigDecimal requirePositive(BigDecimal value) {
		if (value == null || value.signum() <= 0) {
			throw new IllegalArgumentException("Value must be greater than zero");
		}
		return value;
	}

}
