package com.seanglay.fuelstation.accounting.presentation.dto;

import java.math.BigDecimal;

import com.seanglay.fuelstation.accounting.domain.AccountCode;
import com.seanglay.fuelstation.accounting.domain.JournalLine;

public record JournalLineResponse(AccountCode account, BigDecimal debit, BigDecimal credit) {

	public static JournalLineResponse from(JournalLine line) {
		return new JournalLineResponse(line.getAccount(), line.getDebit(), line.getCredit());
	}

}
