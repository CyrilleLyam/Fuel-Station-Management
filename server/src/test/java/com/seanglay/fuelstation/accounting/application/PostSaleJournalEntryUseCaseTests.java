package com.seanglay.fuelstation.accounting.application;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.seanglay.fuelstation.accounting.domain.AccountBalance;
import com.seanglay.fuelstation.accounting.domain.AccountCode;
import com.seanglay.fuelstation.accounting.domain.JournalEntry;
import com.seanglay.fuelstation.accounting.domain.JournalEntryRepository;
import com.seanglay.fuelstation.shared.domain.PageResult;

import static org.assertj.core.api.Assertions.assertThat;

class PostSaleJournalEntryUseCaseTests {

	private final StubJournalEntryRepository repository = new StubJournalEntryRepository();

	private final PostSaleJournalEntryUseCase useCase = new PostSaleJournalEntryUseCase(repository);

	@Test
	void postsABalancedEntryAgainstTheSettlementAccount() {
		Optional<JournalEntry> entry = post(UUID.randomUUID(), new BigDecimal("46.00"));

		assertThat(entry).isPresent();
		assertThat(entry.get().getTotalDebit()).isEqualByComparingTo("46.00");
		assertThat(entry.get().getTotalCredit()).isEqualByComparingTo("46.00");
		assertThat(entry.get().getLines()).extracting(line -> line.getAccount())
			.containsExactly(AccountCode.CASH, AccountCode.FUEL_SALES_REVENUE);
	}

	@Test
	void redeliveryOfTheSameSaleDoesNotDoublePost() {
		UUID reference = UUID.randomUUID();

		assertThat(post(reference, new BigDecimal("46.00"))).isPresent();
		assertThat(post(reference, new BigDecimal("46.00"))).isEmpty();
		assertThat(repository.saved).hasSize(1);
	}

	@Test
	void skipsAZeroValueSaleInsteadOfFailingForever() {
		assertThat(post(UUID.randomUUID(), BigDecimal.ZERO)).isEmpty();
		assertThat(repository.saved).isEmpty();
	}

	private Optional<JournalEntry> post(UUID saleReference, BigDecimal totalAmount) {
		return useCase.execute(saleReference, 1L, LocalDate.of(2026, 9, 1), AccountCode.CASH, totalAmount);
	}

	private static final class StubJournalEntryRepository implements JournalEntryRepository {

		private final List<JournalEntry> saved = new ArrayList<>();

		@Override
		public Optional<JournalEntry> findById(Long id) {
			return Optional.empty();
		}

		@Override
		public boolean existsByReference(String reference) {
			return saved.stream().anyMatch(entry -> entry.getReference().equals(reference));
		}

		@Override
		public PageResult<JournalEntry> search(Long stationId, LocalDate from, LocalDate to, int page, int size) {
			return new PageResult<>(List.copyOf(saved), page, size, saved.size(), 1);
		}

		@Override
		public List<AccountBalance> trialBalance(Long stationId, LocalDate from, LocalDate to) {
			return List.of();
		}

		@Override
		public JournalEntry save(JournalEntry entry) {
			saved.add(entry);
			return entry;
		}

	}

}
