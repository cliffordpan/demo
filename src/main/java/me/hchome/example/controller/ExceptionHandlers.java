package me.hchome.example.controller;

import jakarta.persistence.EntityNotFoundException;
import me.hchome.example.dto.ErrorEntity;
import me.hchome.example.exception.ServiceNotAvailable;
import me.hchome.example.exception.TokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

/**
 * @author Cliff Pan
 * @since
 */
@RestControllerAdvice
public class ExceptionHandlers {
	private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlers.class);

	@ExceptionHandler(TokenException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorEntity handleTokenException(TokenException exception) {
		LOG.debug("Token exception", exception);
		return createErrorEntity(exception.getMessage(), getSimpleName(exception));
	}

	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorEntity handleEntityNotFoundException(EntityNotFoundException exception) {
		LOG.debug("Entity Not found", exception);
		return createErrorEntity(exception.getMessage(), getSimpleName(exception));
	}

	@ExceptionHandler(ServiceNotAvailable.class)
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	public ErrorEntity handleServiceNotAvailable(ServiceNotAvailable ex) {
		LOG.debug("Service not available", ex);
		return createErrorEntity(ex.getMessage(), getSimpleName(ex));
	}

	private String getSimpleName(Throwable throwable) {
		return throwable.getClass().getSimpleName();
	}

	private ErrorEntity createErrorEntity(String clientMessage, String code) {
		return new ErrorEntity(clientMessage, code, Instant.now());
	}
}
