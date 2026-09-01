package com.seanglay.fuelstation.tank.infrastructure;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import com.seanglay.fuelstation.sales.SaleCompleted;
import com.seanglay.fuelstation.tank.application.ApplySaleToInventoryUseCase;

@Component
class SaleInventoryListener {

	private static final Logger log = LoggerFactory.getLogger(SaleInventoryListener.class);

	private final ApplySaleToInventoryUseCase applySaleToInventoryUseCase;

	SaleInventoryListener(ApplySaleToInventoryUseCase applySaleToInventoryUseCase) {
		this.applySaleToInventoryUseCase = applySaleToInventoryUseCase;
	}

	@ApplicationModuleListener
	void on(SaleCompleted event) {
		BigDecimal shortfall = applySaleToInventoryUseCase.execute(event.tankId(), event.productId(), event.quantity());

		if (shortfall.signum() > 0) {
			log.warn("Tank {} was short {} on sale {}; book quantity floored at zero and needs a physical dip check",
					event.tankId(), shortfall, event.reference());
		}
	}

}
