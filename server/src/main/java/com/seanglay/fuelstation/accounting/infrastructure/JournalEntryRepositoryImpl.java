package com.seanglay.fuelstation.accounting.infrastructure;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.seanglay.fuelstation.accounting.domain.AccountBalance;
import com.seanglay.fuelstation.accounting.domain.JournalEntry;
import com.seanglay.fuelstation.accounting.domain.JournalEntryRepository;
import com.seanglay.fuelstation.shared.domain.PageResult;

@Repository
class JournalEntryRepositoryImpl implements JournalEntryRepository {

	private static final LocalDate OPEN_START = LocalDate.of(1970, 1, 1);

	private static final LocalDate OPEN_END = LocalDate.of(9999, 12, 31);

	private final JournalEntryJpaRepository jpaRepository;

	JournalEntryRepositoryImpl(JournalEntryJpaRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	@Override
	public Optional<JournalEntry> findById(Long id) {
		return jpaRepository.findById(id);
	}

	@Override
	public boolean existsByReference(String reference) {
		return jpaRepository.existsByReference(reference);
	}

	@Override
	public PageResult<JournalEntry> search(Long stationId, LocalDate from, LocalDate to, int page, int size) {
		Page<JournalEntry> result = jpaRepository.search(stationId, start(from), end(to), PageRequest.of(page, size));
		return new PageResult<>(result.getContent(), result.getNumber(), result.getSize(), result.getTotalElements(),
				result.getTotalPages());
	}

	@Override
	public List<AccountBalance> trialBalance(Long stationId, LocalDate from, LocalDate to) {
		return jpaRepository.trialBalance(stationId, start(from), end(to));
	}

	@Override
	public JournalEntry save(JournalEntry entry) {
		return jpaRepository.save(entry);
	}

	private static LocalDate start(LocalDate from) {
		return from == null ? OPEN_START : from;
	}

	private static LocalDate end(LocalDate to) {
		return to == null ? OPEN_END : to;
	}

}
