package com.seanglay.fuelstation.shared.presentation;

public record ApiResponse<T>(boolean error, String message, T data) {

	public static <T> ApiResponse<T> ok(String message, T data) {
		return new ApiResponse<>(false, message, data);
	}

	public static ApiResponse<Void> error(String message) {
		return new ApiResponse<>(true, message, null);
	}

}
