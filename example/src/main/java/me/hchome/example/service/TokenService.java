package me.hchome.example.service;

import com.nimbusds.jose.jwk.JWK;
import jakarta.servlet.http.HttpServletRequest;
import me.hchome.example.dto.AccountDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;

import java.util.function.Consumer;

/**
 * Generate token/issuer for jwt authentication
 *
 * @author Cliff Pan
 */
public interface TokenService {


	/**
	 * Construct issuer url from request
	 */
	String getIssuerUrl(HttpServletRequest request);

	/**
	 * Generate jwt token, no multi-tenancy implementation, always use same rsa keypair for the host
	 *
	 * @param issuer issuer url
	 * @param handler token builder handler, to build access/refresh tokens
	 */
	Jwt generateToken(String issuer, Consumer<JwtClaimsSet.Builder> handler);

	Jwt generateAccessToken(AccountDetails account, String issuerUrl);

	Jwt generateRefreshToken(AccountDetails account, String issuerUrl);
}
