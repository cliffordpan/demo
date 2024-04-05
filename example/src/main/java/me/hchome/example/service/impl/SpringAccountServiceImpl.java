package me.hchome.example.service.impl;

import me.hchome.example.dao.AccountRepository;
import me.hchome.example.model.Account;
import me.hchome.example.model.Client;
import me.hchome.example.model.Employee;
import me.hchome.example.utils.AccountDetailsUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Cliff Pan
 * @since
 */
@Service
@Transactional
class SpringAccountServiceImpl implements UserDetailsService {

	private final PasswordEncoder encoder;

	private final AccountRepository<? extends Account> repository;

	public SpringAccountServiceImpl(PasswordEncoder encoder, @Qualifier("accountRepository") AccountRepository<? extends Account> repository) {
		this.encoder = encoder;
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = repository.findOneByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
		if (account instanceof Client client) {
			return AccountDetailsUtils.from(client);
		} else if (account instanceof Employee employee) {
			return AccountDetailsUtils.from(employee);
		} else {
			throw new UsernameNotFoundException(username + " not found");
		}
	}
}
