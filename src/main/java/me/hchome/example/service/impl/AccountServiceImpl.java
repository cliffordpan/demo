package me.hchome.example.service.impl;

import jakarta.persistence.EntityNotFoundException;
import me.hchome.example.dao.AccountRepository;
import me.hchome.example.dao.ClientRepository;
import me.hchome.example.dao.EmployeeRepository;
import me.hchome.example.model.Account;
import me.hchome.example.model.Client;
import me.hchome.example.model.Employee;
import me.hchome.example.service.AccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Cliff Pan
 * @since
 */
@Transactional
@Service
class AccountServiceImpl implements AccountService {

	private final EmployeeRepository employeeRepository;
	private final ClientRepository clientRepository;
	private final PasswordEncoder encoder;

	public AccountServiceImpl(EmployeeRepository employeeRepository, ClientRepository clientRepository, PasswordEncoder encoder) {
		this.employeeRepository = employeeRepository;
		this.clientRepository = clientRepository;
		this.encoder = encoder;
	}

	@Override
	public Account getProfile(Account.Type type, String email) {
		return selectRepository(type).findOneByEmail(email).orElseThrow(() -> new EntityNotFoundException("Account isn't exist for email: " + email));
	}

	@Override
	public boolean isPasswordMatch(Account.Type type, String email, String p) {
		Account account = getProfile(type, email);
		return encoder.matches(p, account.getPassword());
	}

	@Override
	public Account updatePassword(Account.Type type, String email, String old, String newPassword) {
		if (!isPasswordMatch(type, email, old)) {
			throw new BadCredentialsException("Password isn't match");
		}
		Account account = getProfile(type, email);
		account.setPassword(encoder.encode(newPassword));
		if (account instanceof Client client) {
			return clientRepository.save(client);
		} else if (account instanceof Employee employee) {
			return employeeRepository.save(employee);
		}
		throw new UsernameNotFoundException("Cannot find account for update password: " + email);
	}

	@Override
	public Account updateAccount(Account.Type type, String email, Account account) {
		Account dbAccount = getProfile(type, email);
		if (account instanceof Client client && dbAccount instanceof Client dbClient) {
			BeanUtils.copyProperties(client, dbClient, "id", "password", "type");

			dbAccount = clientRepository.save(dbClient);
			return dbAccount;
		} else if (account instanceof Employee employee && dbAccount instanceof Employee dbEmployee) {
			BeanUtils.copyProperties(employee, dbEmployee, "id", "password", "type");
			dbAccount = employeeRepository.save(dbEmployee);
			return dbAccount;
		}
		throw new UsernameNotFoundException("Cannot find account for update password: " + email);
	}



	private AccountRepository<? extends Account> selectRepository(Account.Type type) {
		return switch (type) {
			case EMPLOYEE -> employeeRepository;
			case CLIENT -> clientRepository;
		};
	}
}
