package com.seanglay.fuelstation.sales.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

import com.seanglay.fuelstation.sales.PaymentMethod;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.within;

class SaleTests {

	@Test
	void multipliesQuantityByPriceAndRoundsToCents() {
		Sale sale = sale(new BigDecimal("40.125"), new BigDecimal("1.150"));

		assertThat(sale.getTotalAmount()).isEqualByComparingTo("46.14");
	}

	@Test
	void assignsATimeOrderedReference() {
		Sale first = sale(BigDecimal.ONE, BigDecimal.ONE);
		Sale second = sale(BigDecimal.ONE, BigDecimal.ONE);

		assertThat(first.getReference()).isNotNull().isNotEqualTo(second.getReference());
		assertThat(first.getReference().version()).isEqualTo(7);
	}

	@Test
	void defaultsSoldAtToNow() {
		Sale sale = new Sale(1L, 2L, 3L, "attendant", BigDecimal.ONE, BigDecimal.ONE, PaymentMethod.CASH, null);

		assertThat(sale.getSoldAt()).isCloseTo(Instant.now(), within(5, ChronoUnit.SECONDS));
	}

	@Test
	void rejectsNonPositiveQuantity() {
		assertThatIllegalArgumentException().isThrownBy(() -> sale(BigDecimal.ZERO, BigDecimal.ONE))
			.withMessageContaining("Quantity");
	}

	@Test
	void rejectsNegativePrice() {
		assertThatIllegalArgumentException().isThrownBy(() -> sale(BigDecimal.ONE, new BigDecimal("-0.01")))
			.withMessageContaining("Unit price");
	}

	@Test
	void rejectsBlankAttendant() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> new Sale(1L, 2L, 3L, " ", BigDecimal.ONE, BigDecimal.ONE, PaymentMethod.CASH, null))
			.withMessageContaining("Attendant");
	}

	private static Sale sale(BigDecimal quantity, BigDecimal unitPrice) {
		return new Sale(1L, 2L, 3L, "attendant", quantity, unitPrice, PaymentMethod.CASH, Instant.now());
	}

}
