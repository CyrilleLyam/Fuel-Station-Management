package com.seanglay.fuelstation.accounting.presentation;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seanglay.fuelstation.accounting.application.GetTrialBalanceUseCase;
import com.seanglay.fuelstation.accounting.application.ListJournalEntriesUseCase;
import com.seanglay.fuelstation.accounting.domain.JournalEntry;
import com.seanglay.fuelstation.accounting.presentation.dto.AccountBalanceResponse;
import com.seanglay.fuelstation.accounting.presentation.dto.JournalEntryResponse;
import com.seanglay.fuelstation.iam.RequiresPermission;
import com.seanglay.fuelstation.shared.domain.PageResult;
import com.seanglay.fuelstation.shared.presentation.ApiResponse;
import com.seanglay.fuelstation.shared.presentation.PageMeta;

@RestController
@RequestMapping("/accounting")
class JournalEntryController {

	private final ListJournalEntriesUseCase listJournalEntriesUseCase;

	private final GetTrialBalanceUseCase getTrialBalanceUseCase;

	JournalEntryController(ListJournalEntriesUseCase listJournalEntriesUseCase,
			GetTrialBalanceUseCase getTrialBalanceUseCase) {
		this.listJournalEntriesUseCase = listJournalEntriesUseCase;
		this.getTrialBalanceUseCase = getTrialBalanceUseCase;
	}

	@GetMapping("/journal-entries")
	@RequiresPermission(resource = "accounting", action = "read")
	ApiResponse<List<JournalEntryResponse>> listEntries(@RequestParam(required = false) Long stationId,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
		PageResult<JournalEntry> entries = listJournalEntriesUseCase.execute(stationId, from, to, page, size);
		return ApiResponse.ok("Journal entries retrieved", entries.map(JournalEntryResponse::from).content(),
				PageMeta.from(entries));
	}

	@GetMapping("/trial-balance")
	@RequiresPermission(resource = "accounting", action = "read")
	ApiResponse<List<AccountBalanceResponse>> trialBalance(@RequestParam(required = false) Long stationId,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
		List<AccountBalanceResponse> balances = getTrialBalanceUseCase.execute(stationId, from, to)
			.stream()
			.map(AccountBalanceResponse::from)
			.toList();
		return ApiResponse.ok("Trial balance retrieved", balances);
	}

}
