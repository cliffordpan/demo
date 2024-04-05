package me.hchome.example.config.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Extract refresh token from cookie, fallback to spring default {@link DefaultBearerTokenResolver} if cannot retrieve token
 * from cookie
 *
 * @author Cliff Pan
 */
public class CookieRefreshTokenResolver implements BearerTokenResolver {

	private final BearerTokenResolver delegateResolver = new DefaultBearerTokenResolver();
	private final AntPathRequestMatcher matcher;
	private final String cookieName;

	public CookieRefreshTokenResolver(String path, String cookieName) {
		this.matcher = new AntPathRequestMatcher(path, "POST");
		this.cookieName = cookieName;
	}

	@Override
	public String resolve(HttpServletRequest request) {
		if (matcher.matches(request) && request.getCookies() != null) {
			String token = null;
			for(Cookie cookie: request.getCookies()) {
				if(cookie.getName().equalsIgnoreCase(cookieName)) {
					token = cookie.getValue();
					break;
				}
			}
			if(token != null && !token.isBlank()) {
				return token;
			}
		}
		return delegateResolver.resolve(request);
	}
}
