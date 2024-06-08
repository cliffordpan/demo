package me.hchome.example.config.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Extract refresh token from cookie, fallback to spring default
 * {@link DefaultBearerTokenResolver} if cannot retrieve token.
 * from cookie
 *
 * @author Cliff Pan
 */
class CookieRefreshTokenResolver implements BearerTokenResolver {

    /**
     * Default bearer token resolver.
     */
    private final BearerTokenResolver delegateResolver
            = new DefaultBearerTokenResolver();
    /**
     * Spring path matcher.
     */
    private final AntPathRequestMatcher matcher;
    /**
     * Refresh token cookie name.
     */
    private final String cookieName;

    CookieRefreshTokenResolver(final String path,
                               final String cookieNameInput) {
        this.matcher = new AntPathRequestMatcher(path, "POST");
        this.cookieName = cookieNameInput;
    }

    @Override
    public String resolve(final HttpServletRequest request) {
        if (matcher.matches(request) && request.getCookies() != null) {
            String token = null;
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equalsIgnoreCase(cookieName)) {
                    token = cookie.getValue();
                    break;
                }
            }
            if (token != null && !token.isBlank()) {
                return token;
            }
        }
        return delegateResolver.resolve(request);
    }
}
