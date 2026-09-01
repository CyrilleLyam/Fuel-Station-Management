package com.seanglay.fuelstation.accounting.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import com.seanglay.fuelstation.shared.domain.AggregateRoot;

@Entity
@Table(name = "journal_entries")
public class JournalEntry extends AggregateRoot<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "reference", nullable = false, unique = true, updatable = false)
	private String reference;

	@Column(name = "station_id", nullable = false)
	private Long stationId;

	@Column(name = "entry_date", nullable = false)
	private LocalDate entryDate;

	@Column(name = "memo", nullable = false)
	private String memo;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "journal_entry_lines", joinColumns = @JoinColumn(name = "journal_entry_id"))
	@OrderColumn(name = "line_number")
	private List<JournalLine> lines = new ArrayList<>();

	protected JournalEntry() {
	}

	public JournalEntry(String reference, Long stationId, LocalDate entryDate, String memo, List<JournalLine> lines) {
		this.reference = reference;
		this.stationId = stationId;
		this.entryDate = entryDate;
		this.memo = memo;
		this.lines = new ArrayList<>(requireBalanced(lines));
	}

	@Override
	public Long getId() {
		return id;
	}

	public String getReference() {
		return reference;
	}

	public Long getStationId() {
		return stationId;
	}

	public LocalDate getEntryDate() {
		return entryDate;
	}

	public String getMemo() {
		return memo;
	}

	public List<JournalLine> getLines() {
		return List.copyOf(lines);
	}

	public BigDecimal getTotalDebit() {
		return total(JournalLine::getDebit);
	}

	public BigDecimal getTotalCredit() {
		return total(JournalLine::getCredit);
	}

	private BigDecimal total(Function<JournalLine, BigDecimal> side) {
		return lines.stream().map(side).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private static List<JournalLine> requireBalanced(List<JournalLine> lines) {
		if (lines == null || lines.size() < 2) {
			throw new IllegalArgumentException("A journal entry needs at least two lines");
		}

		BigDecimal debit = lines.stream().map(JournalLine::getDebit).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal credit = lines.stream().map(JournalLine::getCredit).reduce(BigDecimal.ZERO, BigDecimal::add);

		if (debit.compareTo(credit) != 0) {
			throw new IllegalArgumentException(
					"Journal entry is unbalanced: debit %s vs credit %s".formatted(debit, credit));
		}

		if (debit.signum() == 0) {
			throw new IllegalArgumentException("Journal entry must move a non-zero amount");
		}

		return lines;
	}

}
