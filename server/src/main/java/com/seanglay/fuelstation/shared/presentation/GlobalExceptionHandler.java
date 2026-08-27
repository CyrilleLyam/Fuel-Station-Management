package com.seanglay.fuelstation.shared.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.seanglay.fuelstation.shared.domain.AlreadyExistsException;
import com.seanglay.fuelstation.shared.domain.NotFoundException;
import com.seanglay.fuelstation.shared.domain.UnauthorizedException;

@RestControllerAdvice
class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(AlreadyExistsException.class)
	ResponseEntity<ApiResponse<Void>> handleAlreadyExists(AlreadyExistsException ex) {
		return status(HttpStatus.CONFLICT, ex.getMessage());
	}

	@ExceptionHandler(NotFoundException.class)
	ResponseEntity<ApiResponse<Void>> handleNotFound(NotFoundException ex) {
		return status(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	@ExceptionHandler(UnauthorizedException.class)
	ResponseEntity<ApiResponse<Void>> handleUnauthorized(UnauthorizedException ex) {
		return status(HttpStatus.UNAUTHORIZED, ex.getMessage());
	}

	@ExceptionHandler({ IllegalArgumentException.class, IllegalStateException.class })
	ResponseEntity<ApiResponse<Void>> handleInvalidOperation(RuntimeException ex) {
		return status(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	ResponseEntity<ApiResponse<Void>> handleDataIntegrity(DataIntegrityViolationException ex) {
		return status(HttpStatus.CONFLICT, "Request violates a data constraint");
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
		String message = ex.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(error -> error.getField() + ": " + error.getDefaultMessage())
			.reduce((a, b) -> a + "; " + b)
			.orElse("Validation failed");
		return status(HttpStatus.BAD_REQUEST, message);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	ResponseEntity<ApiResponse<Void>> handleMalformedBody(HttpMessageNotReadableException ex) {
		return status(HttpStatus.BAD_REQUEST, "Malformed request body");
	}

	@ExceptionHandler(ErrorResponseException.class)
	ResponseEntity<ApiResponse<Void>> handleErrorResponse(ErrorResponseException ex) {
		String message = ex.getBody().getDetail();
		return status(HttpStatus.valueOf(ex.getStatusCode().value()), message != null ? message : ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception ex) {
		log.error("Unhandled exception", ex);
		return status(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
	}

	private static ResponseEntity<ApiResponse<Void>> status(HttpStatus status, String message) {
		return ResponseEntity.status(status).body(ApiResponse.error(message));
	}

}
