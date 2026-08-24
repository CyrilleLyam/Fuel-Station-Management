package com.seanglay.fuelstation.shared.domain;

import java.util.UUID;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;

public final class UuidV7 {

	private static final NoArgGenerator GENERATOR = Generators.timeBasedEpochGenerator();

	private UuidV7() {
	}

	public static UUID randomUUID() {
		return GENERATOR.generate();
	}

}
