package me.hchome.example.config.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 * Clear refresh token if logout
 *
 * @author Cliff Pan
 */
class ClearRefreshTokenLogoutHandler implements LogoutHandler {

	private final String cookeName;
	private final String refreshPath;

	public ClearRefreshTokenLogoutHandler(String cookeName, String refreshPath) {
		this.cookeName = cookeName;
		this.refreshPath = refreshPath;
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		Cookie cookie = new Cookie(cookeName, null);
		cookie.setPath(refreshPath);
		cookie.setMaxAge(0);
		cookie.setSecure(request.isSecure());
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}
}
