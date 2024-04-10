package me.hchome.example.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import me.hchome.example.dto.AccountDetails;
import me.hchome.example.service.TokenService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author Cliff Pan
 */
@Service
class TokenServiceImpl implements TokenService {

	private final JwtEncoder encoder;

	public TokenServiceImpl(JwtEncoder encoder) {
		this.encoder = encoder;
	}

	@Override
	public String getIssuerUrl(HttpServletRequest request) {
		String scheme = request.getScheme();
		String host = request.getServerName();
		int port = request.getServerPort();
		if("http".equalsIgnoreCase(scheme) && port == 80) {
			return "http://" + host;
		} else if ("https".equalsIgnoreCase(scheme) && port == 443) {
			return "https://" + host;
		}
		return String.format("%s://%s:%d", scheme, host, port);
	}

	@Override
	public Jwt generateToken(String host, Consumer<JwtClaimsSet.Builder> handler)  {
		JwsHeader header = JwsHeader.with(SignatureAlgorithm.RS512)
				.build();
		JwtClaimsSet.Builder builder = JwtClaimsSet.builder();
		handler.accept(builder);
		JwtClaimsSet claimsSet = builder.id(UUID.randomUUID().toString()).issuer(host).build();
		return encoder.encode(JwtEncoderParameters.from(header, claimsSet));
	}

	@Override
	public Jwt generateAccessToken(AccountDetails account, String issuerUrl) {
		Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
		return generateToken(issuerUrl, builder -> {
			builder.subject(account.getEmail())
					.issuedAt(now)
					.expiresAt(now.plus(30L, ChronoUnit.MINUTES))
					.claim("uid", account.getId())
					.claim("enb", account.isEnabled())
					.claim("type", account.getType())
					.claim("grants", account.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
		});
	}

	@Override
	public Jwt generateRefreshToken(AccountDetails account, String issuerUrl) {
		Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
		return generateToken(issuerUrl, builder -> {
			builder.subject(account.getEmail())
					.issuedAt(now)
					.expiresAt(now.plus(7L, ChronoUnit.DAYS));
		});
	}


}
