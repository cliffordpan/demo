package me.hchome.example.utils;

import me.hchome.example.dto.AccountDetails;
import me.hchome.example.model.Account;
import me.hchome.example.model.Client;
import me.hchome.example.model.Employee;
import me.hchome.example.model.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Cliff Pan
 * @since
 */
public final class AccountDetailsUtils {
	private AccountDetailsUtils() {
	}

	public static AccountDetails from(Client client) {
		AccountDetails details = new AccountDetails();
		details.setEmail(client.getEmail());
		details.setEnabled(client.isEnabled());
		details.setPassword(client.getPassword());
		details.setId(client.getId());
		details.setType(Account.Type.CLIENT);
		details.setAuthorities(Set.of(Role.CLIENT.asAuthority()));
		return details;
	}

	public static AccountDetails from(Employee employee) {
		AccountDetails details = new AccountDetails();
		details.setEmail(employee.getEmail());
		details.setEnabled(employee.isEnabled());
		details.setPassword(employee.getPassword());
		details.setId(employee.getId());
		details.setType(Account.Type.EMPLOYEE);
		details.setAuthorities(
				employee.getRoles().stream()
						.map(Role::asAuthority)
						.collect(Collectors.toSet()));
		return details;
	}

	public static AccountDetails from(Jwt jwt) {
		AccountDetails details = new AccountDetails();
		details.setEmail(jwt.getSubject());
		details.setId(jwt.getClaim("uid"));
		details.setEnabled(jwt.getClaim("enb"));
		details.setPassword("");
		details.setType(Account.Type.valueOf(jwt.getClaim("type")));
		details.setAuthorities(jwt.<List<String>>getClaim("grants")
						.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
		return details;
	}

	public static Optional<AccountDetails> getAccountDetails() {
		return Optional.ofNullable(SecurityContextHolder.getContext())
				.map(SecurityContext::getAuthentication)
				.map(Authentication::getPrincipal)
				.flatMap(principal -> principal instanceof AccountDetails details? Optional.of(details):Optional.empty());
	}
}
