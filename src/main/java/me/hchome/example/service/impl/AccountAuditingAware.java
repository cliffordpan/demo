package me.hchome.example.service.impl;

import me.hchome.example.dao.AccountRepository;
import me.hchome.example.dto.AccountDetails;
import me.hchome.example.model.Account;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Cliff Pan
 * @since
 */
@Component("auditingAware")
public class AccountAuditingAware implements AuditorAware<Account> {

	private final AccountRepository<Account> repository;

	public AccountAuditingAware(@Qualifier("accountRepository") AccountRepository<Account> repository) {
		this.repository = repository;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@NonNull
	public Optional<Account> getCurrentAuditor() {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication object = context.getAuthentication();
		if (object instanceof UsernamePasswordAuthenticationToken userToken) {
			Object principal = userToken.getPrincipal();
			if (principal instanceof AccountDetails details) {
				return repository.findOneByEmail(details.getEmail());
			}
		}
		return Optional.empty();
	}
}
