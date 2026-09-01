package com.seanglay.fuelstation.accounting.application;

import java.time.LocalDate;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.seanglay.fuelstation.accounting.domain.AccountBalance;
import com.seanglay.fuelstation.accounting.domain.JournalEntryRepository;
import com.seanglay.fuelstation.shared.application.UseCase;

@UseCase
public class GetTrialBalanceUseCase {

	private final JournalEntryRepository journalEntryRepository;

	public GetTrialBalanceUseCase(JournalEntryRepository journalEntryRepository) {
		this.journalEntryRepository = journalEntryRepository;
	}

	@Transactional(readOnly = true)
	public List<AccountBalance> execute(Long stationId, LocalDate from, LocalDate to) {
		return journalEntryRepository.trialBalance(stationId, from, to);
	}

}
