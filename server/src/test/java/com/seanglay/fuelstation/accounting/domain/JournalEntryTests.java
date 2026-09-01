package com.seanglay.fuelstation.accounting.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class JournalEntryTests {

	@Test
	void postsABalancedCashSale() {
		JournalEntry entry = entry(List.of(JournalLine.debit(AccountCode.CASH, new BigDecimal("46.14")),
				JournalLine.credit(AccountCode.FUEL_SALES_REVENUE, new BigDecimal("46.14"))));

		assertThat(entry.getTotalDebit()).isEqualByComparingTo("46.14");
		assertThat(entry.getTotalCredit()).isEqualByComparingTo("46.14");
		assertThat(entry.getLines()).hasSize(2);
	}

	@Test
	void rejectsAnUnbalancedEntry() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> entry(List.of(JournalLine.debit(AccountCode.CASH, new BigDecimal("46.14")),
					JournalLine.credit(AccountCode.FUEL_SALES_REVENUE, new BigDecimal("40.00")))))
			.withMessageContaining("unbalanced");
	}

	@Test
	void rejectsAZeroAmountEntry() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> entry(List.of(JournalLine.debit(AccountCode.CASH, BigDecimal.ZERO),
					JournalLine.credit(AccountCode.FUEL_SALES_REVENUE, BigDecimal.ZERO))))
			.withMessageContaining("non-zero");
	}

	@Test
	void rejectsASingleSidedEntry() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> entry(List.of(JournalLine.debit(AccountCode.CASH, BigDecimal.TEN))))
			.withMessageContaining("at least two lines");
	}

	@Test
	void reportsNetPositionPerAccount() {
		AccountBalance balance = new AccountBalance(AccountCode.CASH, new BigDecimal("120.00"),
				new BigDecimal("20.00"));

		assertThat(balance.net()).isEqualByComparingTo("100.00");
	}

	private static JournalEntry entry(List<JournalLine> lines) {
		return new JournalEntry("SALE-test", 1L, LocalDate.of(2026, 9, 1), "Fuel sale", lines);
	}

}
