package com.seanglay.fuelstation.reporting.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import com.seanglay.fuelstation.reporting.application.RecordSalesFactUseCase;
import com.seanglay.fuelstation.sales.SaleCompleted;

@Component
class SaleReportingListener {

	private static final Logger log = LoggerFactory.getLogger(SaleReportingListener.class);

	private final RecordSalesFactUseCase recordSalesFactUseCase;

	SaleReportingListener(RecordSalesFactUseCase recordSalesFactUseCase) {
		this.recordSalesFactUseCase = recordSalesFactUseCase;
	}

	@ApplicationModuleListener
	void on(SaleCompleted event) {
		recordSalesFactUseCase
			.execute(event.reference(), event.stationId(), event.productId(), event.tankId(), event.attendant(),
					event.quantity(), event.totalAmount(), event.paymentMethod().name(), event.occurredAt())
			.ifPresentOrElse(fact -> log.debug("Recorded sales fact {} for sale {}", fact.getId(), event.reference()),
					() -> log.debug("Sales fact for sale {} already recorded, skipping", event.reference()));
	}

}
