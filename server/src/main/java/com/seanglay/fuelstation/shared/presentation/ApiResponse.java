package com.seanglay.fuelstation.shared.presentation;

public record ApiResponse<T>(boolean error, String message, T data, Object meta) {

	public static <T> ApiResponse<T> ok(String message, T data) {
		return new ApiResponse<>(false, message, data, null);
	}

	public static <T> ApiResponse<T> ok(String message, T data, Object meta) {
		return new ApiResponse<>(false, message, data, meta);
	}

	public static ApiResponse<Void> error(String message) {
		return new ApiResponse<>(true, message, null, null);
	}

}
