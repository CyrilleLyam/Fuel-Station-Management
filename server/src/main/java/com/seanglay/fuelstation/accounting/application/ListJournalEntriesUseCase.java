package com.seanglay.fuelstation.accounting.application;

import java.time.LocalDate;

import org.springframework.transaction.annotation.Transactional;

import com.seanglay.fuelstation.accounting.domain.JournalEntry;
import com.seanglay.fuelstation.accounting.domain.JournalEntryRepository;
import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.PageResult;

@UseCase
public class ListJournalEntriesUseCase {

	private static final int MAX_PAGE_SIZE = 100;

	private static final int DEFAULT_PAGE_SIZE = 20;

	private final JournalEntryRepository journalEntryRepository;

	public ListJournalEntriesUseCase(JournalEntryRepository journalEntryRepository) {
		this.journalEntryRepository = journalEntryRepository;
	}

	@Transactional(readOnly = true)
	public PageResult<JournalEntry> execute(Long stationId, LocalDate from, LocalDate to, int page, int size) {
		int safePage = Math.max(page, 0);
		int safeSize = size < 1 ? DEFAULT_PAGE_SIZE : Math.min(size, MAX_PAGE_SIZE);

		return journalEntryRepository.search(stationId, from, to, safePage, safeSize);
	}

}
