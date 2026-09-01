package com.seanglay.fuelstation.accounting.presentation.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import com.seanglay.fuelstation.accounting.domain.JournalEntry;

public record JournalEntryResponse(Long id, String reference, Long stationId, LocalDate entryDate, String memo,
		List<JournalLineResponse> lines, BigDecimal totalDebit, BigDecimal totalCredit, Instant createdAt,
		Instant updatedAt) {

	public static JournalEntryResponse from(JournalEntry entry) {
		return new JournalEntryResponse(entry.getId(), entry.getReference(), entry.getStationId(), entry.getEntryDate(),
				entry.getMemo(), entry.getLines().stream().map(JournalLineResponse::from).toList(),
				entry.getTotalDebit(), entry.getTotalCredit(), entry.getCreatedAt(), entry.getUpdatedAt());
	}

}
