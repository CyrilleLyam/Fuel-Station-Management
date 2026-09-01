package com.seanglay.fuelstation.sales.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.seanglay.fuelstation.sales.PaymentMethod;
import com.seanglay.fuelstation.shared.domain.AggregateRoot;
import com.seanglay.fuelstation.shared.domain.UuidV7;

@Entity
@Table(name = "sales")
public class Sale extends AggregateRoot<Long> {

	private static final int AMOUNT_SCALE = 2;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "reference", nullable = false, unique = true, updatable = false)
	private UUID reference;

	@Column(name = "station_id", nullable = false)
	private Long stationId;

	@Column(name = "tank_id", nullable = false)
	private Long tankId;

	@Column(name = "product_id", nullable = false)
	private Long productId;

	@Column(name = "attendant", nullable = false)
	private String attendant;

	@Column(name = "quantity", nullable = false)
	private BigDecimal quantity;

	@Column(name = "unit_price", nullable = false)
	private BigDecimal unitPrice;

	@Column(name = "total_amount", nullable = false)
	private BigDecimal totalAmount;

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_method", nullable = false)
	private PaymentMethod paymentMethod;

	@Column(name = "sold_at", nullable = false)
	private Instant soldAt;

	protected Sale() {
	}

	public Sale(Long stationId, Long tankId, Long productId, String attendant, BigDecimal quantity,
			BigDecimal unitPrice, PaymentMethod paymentMethod, Instant soldAt) {
		this.reference = UuidV7.randomUUID();
		this.stationId = requireIdentifier(stationId, "Station");
		this.tankId = requireIdentifier(tankId, "Tank");
		this.productId = requireIdentifier(productId, "Product");
		this.attendant = requireAttendant(attendant);
		this.quantity = requirePositive(quantity);
		this.unitPrice = requireNonNegative(unitPrice);
		this.paymentMethod = requirePaymentMethod(paymentMethod);
		this.soldAt = soldAt == null ? Instant.now() : soldAt;
		this.totalAmount = this.quantity.multiply(this.unitPrice).setScale(AMOUNT_SCALE, RoundingMode.HALF_UP);
	}

	@Override
	public Long getId() {
		return id;
	}

	public UUID getReference() {
		return reference;
	}

	public Long getStationId() {
		return stationId;
	}

	public Long getTankId() {
		return tankId;
	}

	public Long getProductId() {
		return productId;
	}

	public String getAttendant() {
		return attendant;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public Instant getSoldAt() {
		return soldAt;
	}

	private static Long requireIdentifier(Long value, String label) {
		if (value == null) {
			throw new IllegalArgumentException(label + " is required");
		}
		return value;
	}

	private static String requireAttendant(String attendant) {
		if (attendant == null || attendant.isBlank()) {
			throw new IllegalArgumentException("Attendant is required");
		}
		return attendant;
	}

	private static PaymentMethod requirePaymentMethod(PaymentMethod paymentMethod) {
		if (paymentMethod == null) {
			throw new IllegalArgumentException("Payment method is required");
		}
		return paymentMethod;
	}

	private static BigDecimal requirePositive(BigDecimal quantity) {
		if (quantity == null || quantity.signum() <= 0) {
			throw new IllegalArgumentException("Quantity must be greater than zero");
		}
		return quantity;
	}

	private static BigDecimal requireNonNegative(BigDecimal unitPrice) {
		if (unitPrice == null || unitPrice.signum() < 0) {
			throw new IllegalArgumentException("Unit price must be zero or greater");
		}
		return unitPrice;
	}

}
