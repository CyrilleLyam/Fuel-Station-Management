package com.seanglay.fuelstation.accounting.domain;

import java.math.BigDecimal;

public record AccountBalance(AccountCode account, BigDecimal debit, BigDecimal credit) {

	public BigDecimal net() {
		return debit.subtract(credit);
	}

}
