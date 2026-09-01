package com.seanglay.fuelstation.reporting.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.seanglay.fuelstation.shared.domain.AggregateRoot;

@Entity
@Table(name = "sales_facts")
public class SalesFact extends AggregateRoot<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "sale_reference", nullable = false, unique = true, updatable = false)
	private UUID saleReference;

	@Column(name = "station_id", nullable = false)
	private Long stationId;

	@Column(name = "product_id", nullable = false)
	private Long productId;

	@Column(name = "tank_id", nullable = false)
	private Long tankId;

	@Column(name = "attendant", nullable = false)
	private String attendant;

	@Column(name = "business_date", nullable = false)
	private LocalDate businessDate;

	@Column(name = "quantity", nullable = false)
	private BigDecimal quantity;

	@Column(name = "total_amount", nullable = false)
	private BigDecimal totalAmount;

	@Column(name = "payment_method", nullable = false)
	private String paymentMethod;

	@Column(name = "occurred_at", nullable = false)
	private Instant occurredAt;

	protected SalesFact() {
	}

	public SalesFact(UUID saleReference, Long stationId, Long productId, Long tankId, String attendant,
			BigDecimal quantity, BigDecimal totalAmount, String paymentMethod, Instant occurredAt) {
		this.saleReference = saleReference;
		this.stationId = stationId;
		this.productId = productId;
		this.tankId = tankId;
		this.attendant = attendant;
		this.quantity = quantity;
		this.totalAmount = totalAmount;
		this.paymentMethod = paymentMethod;
		this.occurredAt = occurredAt;
		this.businessDate = LocalDate.ofInstant(occurredAt, ZoneOffset.UTC);
	}

	@Override
	public Long getId() {
		return id;
	}

	public UUID getSaleReference() {
		return saleReference;
	}

	public Long getStationId() {
		return stationId;
	}

	public Long getProductId() {
		return productId;
	}

	public Long getTankId() {
		return tankId;
	}

	public String getAttendant() {
		return attendant;
	}

	public LocalDate getBusinessDate() {
		return businessDate;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public Instant getOccurredAt() {
		return occurredAt;
	}

}
