package com.seanglay.fuelstation.tank.domain;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class TankSaleTests {

	@Test
	void drawsDownStockAndReportsNoShortfall() {
		Tank tank = stockedTank(new BigDecimal("1000.000"));

		BigDecimal shortfall = tank.recordSale(new BigDecimal("250.500"));

		assertThat(shortfall).isEqualByComparingTo("0");
		assertThat(tank.getCurrentQuantity()).isEqualByComparingTo("749.500");
	}

	@Test
	void floorsAtZeroAndReportsShortfallWhenBooksLagBehindThePump() {
		Tank tank = stockedTank(new BigDecimal("100.000"));

		BigDecimal shortfall = tank.recordSale(new BigDecimal("140.000"));

		assertThat(shortfall).isEqualByComparingTo("40.000");
		assertThat(tank.getCurrentQuantity()).isEqualByComparingTo("0");
	}

	@Test
	void keepsRecordDispenseStrictForManualCorrections() {
		Tank tank = stockedTank(new BigDecimal("100.000"));

		assertThat(tank.getAvailableSpace()).isEqualByComparingTo("900.000");
		assertThatIllegalArgumentException().isThrownBy(() -> tank.recordDispense(new BigDecimal("140.000")));
	}

	private static Tank stockedTank(BigDecimal quantity) {
		Tank tank = new Tank(1L, "Tank A", new BigDecimal("1000.000"));
		tank.assignProduct(2L);
		tank.recordDelivery(quantity);
		return tank;
	}

}
