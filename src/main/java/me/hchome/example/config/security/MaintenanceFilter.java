package me.hchome.example.config.security;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.hchome.example.exception.ServiceNotAvailable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * Filter for maintenance state.
 *
 * @author Cliff Pan
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class MaintenanceFilter extends OncePerRequestFilter {

    /**
     * Application availability.
     */
    private final ApplicationAvailability availability;
    /**
     * Exception resolver.
     */
    private final HandlerExceptionResolver exceptionResolver;

    MaintenanceFilter(
            final ApplicationAvailability applicationAvailability,
            //@formatter:off
            @Qualifier("handlerExceptionResolver")
            final HandlerExceptionResolver er
            //@formatter:on
    ) {
        this.availability = applicationAvailability;
        this.exceptionResolver = er;
    }


    @Override
    protected void doFilterInternal(@Nonnull final HttpServletRequest request,
                                    @Nonnull final HttpServletResponse response,
                                    @Nonnull final FilterChain filterChain)
            throws ServletException, IOException {
        if (availability.getReadinessState()
                .equals(ReadinessState.REFUSING_TRAFFIC)
                && (!"/api/server/status"
                .equalsIgnoreCase(request.getRequestURI())
                || !"/api/server/health"
                .equalsIgnoreCase(request.getRequestURI()))) {
            exceptionResolver.resolveException(
                    request, response, null, new ServiceNotAvailable()
            );
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
