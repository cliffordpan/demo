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
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

import java.io.IOException;
import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Base security filtering, more detail controls in controllers.
 *
 * @author Cliff Pan
 */
@SuppressWarnings({"checkstyle:JavadocStyle", "checkstyle:LineLength"})
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    /**
     * This is the API PATH.
     */
    public static final String API_PATHS = "/api/**";
    /**
     * This is Auth Path.
     */
    public static final String AUTH_PATHS = "/auth/**";
    /**
     * This is AUTH Token url.
     */
    public static final String LOGIN_PATH = "/auth/token";
    /**
     * This is AUTH refresh url.
     */
    public static final String REFRESH_PATH = "/auth/refresh";
    /**
     * This is AUTH clear url.
     */
    public static final String LOGOUT_PATH = "/auth/clear";
    /**
     * This is AUTH refresh_token url.
     */
    public static final String REFRESH_COOKIE_NAME = "refresh_token";

    /**
     * This method control the api security.
     *
     * @param http    spring http security
     * @param decoder a jwt decode
     * @return Security filter chain
     * @throws Exception throw if any exception occurred.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurity(
            final HttpSecurity http,
            final JwtDecoder decoder) throws Exception {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(source ->
                source.<List<String>>getClaim("grants")
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet())
        );
        return http.securityMatcher(API_PATHS)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET,
                                "/api/products",
                                "/api/products/{id:\\d+}",
                                "/api/server/health",
                                "/api/server/status",
                                "/api/server/db/reset"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers
                        .xssProtection(xss -> xss
                                .headerValue(
                                        XXssProtectionHeaderWriter
                                                .HeaderValue
                                                .ENABLED_MODE_BLOCK
                                ))
                        .contentSecurityPolicy(
                                cps -> cps
                                        .policyDirectives("script-src 'self'"))
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .anonymous(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(oauth -> oauth.jwt(jwt ->
                        jwt.decoder(decoder)
                                .jwtAuthenticationConverter(
                                        converter.andThen(
                                                new MapJwtToUsernameConverter())
                                )

                ))
                .build();
    }

    /**
     * This method control the auth security.
     *
     * @param http    spring http security
     * @param decoder a jwt decode
     * @return Security filter chain
     * @throws Exception throw if any exception occurred.
     */
    @Bean
    @Order(2)
    public SecurityFilterChain authSecurity(
            final HttpSecurity http,
            final JwtDecoder decoder) throws Exception {
        return http.securityMatcher(AUTH_PATHS)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                HttpMethod.POST,
                                LOGIN_PATH,
                                REFRESH_PATH
                        )
                        .authenticated()
                        .requestMatchers(HttpMethod.POST, LOGOUT_PATH)
                        .permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer(oauth ->
                        oauth.jwt(jwt ->
                                        jwt.decoder(decoder)
                                )
                                .bearerTokenResolver(
                                        new CookieRefreshTokenResolver(
                                                REFRESH_PATH,
                                                REFRESH_COOKIE_NAME)
                                )
                )
                .sessionManagement(session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .logout(logout -> logout
                        .logoutSuccessHandler(
                                new HttpStatusReturningLogoutSuccessHandler(
                                        HttpStatus.NO_CONTENT
                                )
                        )
                        .clearAuthentication(true)
                        .logoutUrl(LOGOUT_PATH)
                        .addLogoutHandler(new ClearRefreshTokenLogoutHandler(
                                REFRESH_COOKIE_NAME, REFRESH_PATH))
                ).build();
    }

    /**
     * Create a JWK object from classpath private key and public key.
     *
     * @return JWK object
     * @throws IOException
     * @throws JOSEException
     */
    @Bean
    public JWK jwk() throws IOException, JOSEException {
        try (InputStream pvis = new ClassPathResource("private.key")
                .getInputStream();
             InputStream pbis = new ClassPathResource("public.pub")
                     .getInputStream()
        ) {
            RSAPrivateKey priKey = EncryptionUtils
                    .readKey(pvis, RSAPrivateKey.class);
            RSAPublicKey pubKey = EncryptionUtils
                    .readKey(pbis, RSAPublicKey.class);
            return new RSAKey.Builder(pubKey).privateKey(priKey)
                    .algorithm(JWSAlgorithm.RS512)
                    .keyUse(KeyUse.SIGNATURE)
                    .keyIDFromThumbprint()
                    .build();
        }
    }

    /**
     * Create a jwt encoder.
     *
     * @param jwk jwk object
     * @return Jwt encoder
     */
    @Bean
    public JwtEncoder encoder(final JWK jwk) {
        JWKSource<SecurityContext> source =
                new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(source);
    }

    /**
     * create a jwt decoder.
     *
     * @param jwk jwk object
     * @return Jwt decoder
     * @throws JOSEException
     */
    @Bean
    public JwtDecoder decoder(final JWK jwk) throws JOSEException {
        RSAPublicKey publicKey = jwk.toRSAKey().toRSAPublicKey();
        return NimbusJwtDecoder.withPublicKey(publicKey)
                .signatureAlgorithm(SignatureAlgorithm.RS512).build();
    }

    /**
     * Password encode from spring security.
     *
     * @return default password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
