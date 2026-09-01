package com.seanglay.fuelstation.reporting.application;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.seanglay.fuelstation.reporting.domain.SalesFact;
import com.seanglay.fuelstation.reporting.domain.SalesFactRepository;
import com.seanglay.fuelstation.shared.application.UseCase;

@UseCase
public class RecordSalesFactUseCase {

	private final SalesFactRepository salesFactRepository;

	public RecordSalesFactUseCase(SalesFactRepository salesFactRepository) {
		this.salesFactRepository = salesFactRepository;
	}

	@Transactional
	public Optional<SalesFact> execute(UUID saleReference, Long stationId, Long productId, Long tankId,
			String attendant, BigDecimal quantity, BigDecimal totalAmount, String paymentMethod, Instant occurredAt) {
		if (salesFactRepository.existsBySaleReference(saleReference)) {
			return Optional.empty();
		}

		return Optional.of(salesFactRepository.save(new SalesFact(saleReference, stationId, productId, tankId,
				attendant, quantity, totalAmount, paymentMethod, occurredAt)));
	}

}
