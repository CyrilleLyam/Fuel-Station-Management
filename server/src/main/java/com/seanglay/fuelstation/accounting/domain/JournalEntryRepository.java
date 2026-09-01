package com.seanglay.fuelstation.accounting.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.seanglay.fuelstation.shared.domain.PageResult;

public interface JournalEntryRepository {

	Optional<JournalEntry> findById(Long id);

	boolean existsByReference(String reference);

	PageResult<JournalEntry> search(Long stationId, LocalDate from, LocalDate to, int page, int size);

	List<AccountBalance> trialBalance(Long stationId, LocalDate from, LocalDate to);

	JournalEntry save(JournalEntry entry);

}
