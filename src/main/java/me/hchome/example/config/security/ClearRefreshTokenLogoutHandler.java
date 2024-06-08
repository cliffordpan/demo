package me.hchome.example.config.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 * Clear refresh token if logout.
 *
 * @author Cliff Pan
 */
class ClearRefreshTokenLogoutHandler implements LogoutHandler {

    /**
     * Cookie name.
     */
    private final String cookeName;
    /**
     * Refresh path.
     */
    private final String refreshPath;

    ClearRefreshTokenLogoutHandler(final String cookieNameInput,
                                   final String refreshPathInput) {
        this.cookeName = cookieNameInput;
        this.refreshPath = refreshPathInput;
    }

    @Override
    public void logout(final HttpServletRequest request,
                       final HttpServletResponse response,
                       final Authentication authentication) {
        Cookie cookie = new Cookie(cookeName, null);
        cookie.setPath(refreshPath);
        cookie.setMaxAge(0);
        cookie.setSecure(request.isSecure());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
