package me.hchome.example.config.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import me.hchome.example.utils.EncryptionUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import java.io.IOException;
import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Base security filtering, more detail controls in controllers
 *
 * @author Cliff Pan
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
	public final static String API_PATHS = "/api/**";
	public final static String AUTH_PATHS = "/auth/**";
	public final static String LOGIN_PATH = "/auth/token";
	public final static String REFRESH_PATH = "/auth/refresh";
	public final static String LOGOUT_PATH = "/auth/clear";
	public final static String REFRESH_COOKIE_NAME = "refresh_token";


	@Bean
	@Order(1)
	public SecurityFilterChain apiSecurity(HttpSecurity http, JwtDecoder decoder) throws Exception {
		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
		converter.setJwtGrantedAuthoritiesConverter(source ->
				source.<List<String>>getClaim("grants").stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
		);
		return http.securityMatcher(API_PATHS)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.GET, "/api/products", "/api/products/{id:\\d+}", "/api/server/health", "/api/server/db/reset").permitAll()
						.anyRequest().authenticated()
				)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.anonymous(AbstractHttpConfigurer::disable)
				.httpBasic(AbstractHttpConfigurer::disable)
				.cors(AbstractHttpConfigurer::disable)
				.csrf(AbstractHttpConfigurer::disable)
				.formLogin(AbstractHttpConfigurer::disable)
				.oauth2ResourceServer(oauth -> oauth.jwt(jwt ->
						jwt.decoder(decoder)
								.jwtAuthenticationConverter(converter.andThen(new MapJwtToUsernameConverter()))

				))
				.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain authSecurity(HttpSecurity http, JwtDecoder decoder) throws Exception {
		return http.securityMatcher(AUTH_PATHS)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.POST, LOGIN_PATH, REFRESH_PATH).authenticated()
						.requestMatchers(HttpMethod.POST, LOGOUT_PATH).permitAll()
						.anyRequest().authenticated())
				.httpBasic(Customizer.withDefaults())
				.oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.decoder(decoder))
						.bearerTokenResolver(new CookieRefreshTokenResolver(REFRESH_PATH, REFRESH_COOKIE_NAME))
				)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.csrf(AbstractHttpConfigurer::disable)
				.cors(Customizer.withDefaults())
				.formLogin(AbstractHttpConfigurer::disable)
				.anonymous(AbstractHttpConfigurer::disable)
				.logout(logout -> logout.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT))
						.clearAuthentication(true)
						.logoutUrl(LOGOUT_PATH)
						.addLogoutHandler(new ClearRefreshTokenLogoutHandler(REFRESH_COOKIE_NAME, REFRESH_PATH))
				).build();
	}

	@Bean
	public JWK jwk() throws IOException, JOSEException {
		try (InputStream pvis = new ClassPathResource("private.key").getInputStream();
			 InputStream pbis = new ClassPathResource("public.pub").getInputStream()
		) {
			RSAPrivateKey priKey = EncryptionUtils.readKey(pvis, RSAPrivateKey.class);
			RSAPublicKey pubKey = EncryptionUtils.readKey(pbis, RSAPublicKey.class);
			return new RSAKey.Builder(pubKey).privateKey(priKey)
					.algorithm(JWSAlgorithm.RS512)
					.keyUse(KeyUse.SIGNATURE)
					.keyIDFromThumbprint()
					.build();
		}
	}

	@Bean
	public JwtEncoder encoder(JWK jwk) {
		JWKSource<SecurityContext> source = new ImmutableJWKSet<>(new JWKSet(jwk));
		return new NimbusJwtEncoder(source);
	}

	@Bean
	public JwtDecoder decoder(JWK jwk) throws JOSEException {
		RSAPublicKey publicKey = jwk.toRSAKey().toRSAPublicKey();
		return NimbusJwtDecoder.withPublicKey(publicKey)
				.signatureAlgorithm(SignatureAlgorithm.RS512).build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

}
