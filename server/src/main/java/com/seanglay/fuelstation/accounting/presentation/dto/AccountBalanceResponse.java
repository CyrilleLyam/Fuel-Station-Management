package com.seanglay.fuelstation.accounting.presentation.dto;

import java.math.BigDecimal;

import com.seanglay.fuelstation.accounting.domain.AccountBalance;
import com.seanglay.fuelstation.accounting.domain.AccountCode;

public record AccountBalanceResponse(AccountCode account, BigDecimal debit, BigDecimal credit, BigDecimal net) {

	public static AccountBalanceResponse from(AccountBalance balance) {
		return new AccountBalanceResponse(balance.account(), balance.debit(), balance.credit(), balance.net());
	}

}
