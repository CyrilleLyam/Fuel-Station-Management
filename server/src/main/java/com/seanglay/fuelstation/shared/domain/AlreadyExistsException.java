package com.seanglay.fuelstation.shared.domain;

public class AlreadyExistsException extends RuntimeException {

	public AlreadyExistsException(String message) {
		super(message);
	}

}
