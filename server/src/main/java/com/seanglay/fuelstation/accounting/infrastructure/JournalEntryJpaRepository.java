package com.seanglay.fuelstation.accounting.infrastructure;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.seanglay.fuelstation.accounting.domain.AccountBalance;
import com.seanglay.fuelstation.accounting.domain.JournalEntry;

interface JournalEntryJpaRepository extends JpaRepository<JournalEntry, Long> {

	boolean existsByReference(String reference);

	@Query("""
			select e from JournalEntry e
			where (:stationId is null or e.stationId = :stationId)
			and e.entryDate between :from and :to
			order by e.entryDate desc, e.id desc
			""")
	Page<JournalEntry> search(@Param("stationId") Long stationId, @Param("from") LocalDate from,
			@Param("to") LocalDate to, Pageable pageable);

	@Query("""
			select new com.seanglay.fuelstation.accounting.domain.AccountBalance(
				l.account, sum(l.debit), sum(l.credit))
			from JournalEntry e join e.lines l
			where (:stationId is null or e.stationId = :stationId)
			and e.entryDate between :from and :to
			group by l.account
			order by l.account asc
			""")
	List<AccountBalance> trialBalance(@Param("stationId") Long stationId, @Param("from") LocalDate from,
			@Param("to") LocalDate to);

}
