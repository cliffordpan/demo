package me.hchome.example.controller;

import jakarta.persistence.EntityNotFoundException;
import me.hchome.example.dto.ErrorEntity;
import me.hchome.example.exception.ServiceNotAvailable;
import me.hchome.example.exception.TokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
	/**
	 * Logger for Exceptions.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(ExceptionHandlers.class);

	/**
	 * Handle auth token generation exception.
	 *
	 * @param exception the token exception
	 * @return wrapped exception
	 */
	@ExceptionHandler(TokenException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorEntity handleTokenException(final TokenException exception) {
		LOG.debug("Token exception", exception);
		return createErrorEntity(exception.getMessage(),
				getSimpleName(exception));
	}

	/**
	 * Handle entity not found exception.
	 *
	 * @param exception the exception
	 * @return wrapped exception
	 */
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorEntity handleEntityNotFoundException(
			final EntityNotFoundException exception) {
		LOG.debug("Entity Not found", exception);
		return createErrorEntity(exception.getMessage(),
				getSimpleName(exception));
	}

	/**
	 * Handle server under maintenance exception.
	 *
	 * @param ex the exception
	 * @return wrapped exception
	 */
	@ExceptionHandler(ServiceNotAvailable.class)
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	public ErrorEntity handleServiceNotAvailable(final ServiceNotAvailable ex) {
		LOG.debug("Service not available", ex);
		return createErrorEntity(ex.getMessage(),
				getSimpleName(ex));
	}

	private String getSimpleName(final Throwable throwable) {
		return throwable.getClass().getSimpleName();
	}

	private ErrorEntity createErrorEntity(
			final String clientMessage,
			final String code) {
		return new ErrorEntity(clientMessage, code, Instant.now());
	}
}
