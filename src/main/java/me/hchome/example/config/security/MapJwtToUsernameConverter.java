package me.hchome.example.config.security;

import jakarta.annotation.Nonnull;
import me.hchome.example.utils.AccountDetailsUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * Convert a Jwt token to username authentication token,
 * extra information from jwt.
 *
 * @author Cliff Pan
 */
class MapJwtToUsernameConverter implements Converter<
        AbstractAuthenticationToken, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(
            @Nonnull final AbstractAuthenticationToken source) {
        if (source instanceof JwtAuthenticationToken jwtToken) {
            Jwt jwt = jwtToken.getToken();
            UserDetails details = AccountDetailsUtils.from(jwt);
            return UsernamePasswordAuthenticationToken.authenticated(details,
                    jwt, details.getAuthorities());
        }
        return source;
    }
}
