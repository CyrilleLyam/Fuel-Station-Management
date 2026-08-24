package com.seanglay.fuelstation;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModularityTests {

	static final ApplicationModules modules = ApplicationModules.of(FuelStationManagementApplication.class);

	@Test
	void verifiesModularStructure() {
		modules.verify();
	}

}
