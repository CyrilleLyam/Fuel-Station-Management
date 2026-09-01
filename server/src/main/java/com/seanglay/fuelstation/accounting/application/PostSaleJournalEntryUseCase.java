package com.seanglay.fuelstation.accounting.application;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.seanglay.fuelstation.accounting.domain.AccountCode;
import com.seanglay.fuelstation.accounting.domain.JournalEntry;
import com.seanglay.fuelstation.accounting.domain.JournalEntryRepository;
import com.seanglay.fuelstation.accounting.domain.JournalLine;
import com.seanglay.fuelstation.shared.application.UseCase;

@UseCase
public class PostSaleJournalEntryUseCase {

	private static final String REFERENCE_PREFIX = "SALE-";

	private final JournalEntryRepository journalEntryRepository;

	public PostSaleJournalEntryUseCase(JournalEntryRepository journalEntryRepository) {
		this.journalEntryRepository = journalEntryRepository;
	}

	@Transactional
	public Optional<JournalEntry> execute(UUID saleReference, Long stationId, LocalDate entryDate,
			AccountCode settlementAccount, BigDecimal totalAmount) {
		if (totalAmount == null || totalAmount.signum() == 0) {
			return Optional.empty();
		}

		String reference = REFERENCE_PREFIX + saleReference;

		if (journalEntryRepository.existsByReference(reference)) {
			return Optional.empty();
		}

		JournalEntry entry = new JournalEntry(reference, stationId, entryDate, "Fuel sale " + saleReference,
				List.of(JournalLine.debit(settlementAccount, totalAmount),
						JournalLine.credit(AccountCode.FUEL_SALES_REVENUE, totalAmount)));

		return Optional.of(journalEntryRepository.save(entry));
	}

}
