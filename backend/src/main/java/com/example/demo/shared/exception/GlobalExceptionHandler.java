package com.example.demo.shared.exception;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(ErrorResponse.of(
						HttpStatus.NOT_FOUND.value(),
						HttpStatus.NOT_FOUND.getReasonPhrase(),
						exception.getMessage()
				));
	}

	@ExceptionHandler(WebExchangeBindException.class)
	public ResponseEntity<ErrorResponse> handleValidation(WebExchangeBindException exception) {
		List<String> details = exception.getFieldErrors()
				.stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage())
				.toList();

		return ResponseEntity.badRequest()
				.body(ErrorResponse.of(
						HttpStatus.BAD_REQUEST.value(),
						HttpStatus.BAD_REQUEST.getReasonPhrase(),
						"Invalid request body",
						details
				));
	}

	@ExceptionHandler({DuplicateKeyException.class, DataIntegrityViolationException.class})
	public ResponseEntity<ErrorResponse> handleDataIntegrity(RuntimeException exception) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(ErrorResponse.of(
						HttpStatus.CONFLICT.value(),
						HttpStatus.CONFLICT.getReasonPhrase(),
						"Product data conflicts with an existing record"
				));
	}

	@ExceptionHandler(ServerWebInputException.class)
	public ResponseEntity<ErrorResponse> handleInvalidInput(ServerWebInputException exception) {
		return ResponseEntity.badRequest()
				.body(ErrorResponse.of(
						HttpStatus.BAD_REQUEST.value(),
						HttpStatus.BAD_REQUEST.getReasonPhrase(),
						exception.getReason()
				));
	}

}
