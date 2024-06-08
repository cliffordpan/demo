package me.hchome.example.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import static me.hchome.example.config.security.WebSecurityConfig.*;

import jakarta.servlet.http.HttpServletResponse;
import me.hchome.example.dto.AccountDetails;
import me.hchome.example.dto.Tokens;
import me.hchome.example.exception.TokenException;
import me.hchome.example.service.TokenService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Objects;

/**
 * @author Cliff Pan
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	private final TokenService tokenService;
	private final UserDetailsService accountService;

	/**
	 * Public constructor for AuthenticationController.
	 *
	 * @param tokenServiceDi   token service dependency injection
	 * @param accountServiceDi account server dependency injection
	 */
	public AuthenticationController(final TokenService tokenServiceDi,
									final UserDetailsService accountServiceDi) {
		this.tokenService = tokenServiceDi;
		this.accountService = accountServiceDi;
	}

	/**
	 * Basic auth for getting the authentication token.
	 *
	 * @param request   http request
	 * @param response  Http response
	 * @param principal authentication principal
	 * @return tokens
	 */
	@PostMapping({"/token", "/refresh"})
	public Tokens tokens(final HttpServletRequest request,
						 final HttpServletResponse response,
						 @AuthenticationPrincipal final Object principal) {
		String issuerUrl = tokenService.getIssuerUrl(request);
		if (principal instanceof AccountDetails account) {
			Jwt access = tokenService.generateAccessToken(account, issuerUrl);
			Jwt refresh = tokenService.generateRefreshToken(account, issuerUrl);
			saveCookie(request, response, refresh);
			return new Tokens(access.getTokenValue(), access.getId());
		} else if (principal instanceof Jwt refresh) {
			UserDetails details = accountService
					.loadUserByUsername(refresh.getSubject());
			if (details instanceof AccountDetails account) {
				Jwt access = tokenService
						.generateAccessToken(account, issuerUrl);
				return new Tokens(access.getTokenValue(), access.getId());
			}
		}
		throw new TokenException("Unable to get token");
	}

	private void saveCookie(final HttpServletRequest request,
							final HttpServletResponse response,
							final Jwt refresh) {
		Cookie cookie = new Cookie(REFRESH_COOKIE_NAME,
				refresh.getTokenValue());
		cookie.setPath(REFRESH_PATH);
		cookie.setSecure(request.isSecure());
		cookie.setHttpOnly(true);
		Duration duration = Duration.between(
				Objects.requireNonNull(refresh.getIssuedAt()),
				refresh.getExpiresAt()
		);
		cookie.setMaxAge((int) duration.toSeconds());
		response.addCookie(cookie);
	}
}
