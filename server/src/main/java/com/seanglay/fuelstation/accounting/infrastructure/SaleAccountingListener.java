package com.seanglay.fuelstation.accounting.infrastructure;

import java.time.LocalDate;
import java.time.ZoneOffset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import com.seanglay.fuelstation.accounting.application.PostSaleJournalEntryUseCase;
import com.seanglay.fuelstation.accounting.domain.AccountCode;
import com.seanglay.fuelstation.sales.PaymentMethod;
import com.seanglay.fuelstation.sales.SaleCompleted;

@Component
class SaleAccountingListener {

	private static final Logger log = LoggerFactory.getLogger(SaleAccountingListener.class);

	private final PostSaleJournalEntryUseCase postSaleJournalEntryUseCase;

	SaleAccountingListener(PostSaleJournalEntryUseCase postSaleJournalEntryUseCase) {
		this.postSaleJournalEntryUseCase = postSaleJournalEntryUseCase;
	}

	@ApplicationModuleListener
	void on(SaleCompleted event) {
		LocalDate entryDate = LocalDate.ofInstant(event.occurredAt(), ZoneOffset.UTC);

		postSaleJournalEntryUseCase
			.execute(event.reference(), event.stationId(), entryDate, settlementAccountFor(event.paymentMethod()),
					event.totalAmount())
			.ifPresentOrElse(
					entry -> log.debug("Posted journal entry {} for sale {}", entry.getReference(), event.reference()),
					() -> log.debug("No journal entry posted for sale {}: already recorded or zero value",
							event.reference()));
	}

	private static AccountCode settlementAccountFor(PaymentMethod paymentMethod) {
		return switch (paymentMethod) {
			case CASH -> AccountCode.CASH;
			case CARD -> AccountCode.CARD_CLEARING;
			case MOBILE -> AccountCode.MOBILE_MONEY;
			case CREDIT -> AccountCode.ACCOUNTS_RECEIVABLE;
		};
	}

}
