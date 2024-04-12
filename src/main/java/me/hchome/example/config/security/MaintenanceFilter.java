package me.hchome.example.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.hchome.example.exception.ServiceNotAvailable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * @author Cliff Pan
 * @since
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MaintenanceFilter extends OncePerRequestFilter {


	private final ApplicationAvailability availability;
	private final HandlerExceptionResolver exceptionResolver;

	public MaintenanceFilter(ApplicationAvailability applicationAvailability, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
		this.availability = applicationAvailability;
		this.exceptionResolver = exceptionResolver;
	}


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		if (availability.getReadinessState().equals(ReadinessState.REFUSING_TRAFFIC) &&
				(!"/api/server/status".equalsIgnoreCase(request.getRequestURI()) ||
						!"/api/server/health".equalsIgnoreCase(request.getRequestURI()))) {
			this.exceptionResolver.resolveException(request, response, null, new ServiceNotAvailable());
		} else {
			filterChain.doFilter(request, response);
		}
	}
}
