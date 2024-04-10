package me.hchome.example.service.impl;

import jakarta.persistence.EntityNotFoundException;
import me.hchome.example.dao.AccountRepository;
import me.hchome.example.dao.ClientRepository;
import me.hchome.example.dao.EmployeeRepository;
import me.hchome.example.model.Account;
import me.hchome.example.service.AccountService;
import org.springframework.stereotype.Service;

/**
 * @author Cliff Pan
 * @since
 */
@Service
class AccountServiceImpl implements AccountService {

	private final EmployeeRepository employeeRepository;
	private final ClientRepository clientRepository;

	public AccountServiceImpl(EmployeeRepository employeeRepository, ClientRepository clientRepository) {
		this.employeeRepository = employeeRepository;
		this.clientRepository = clientRepository;
	}

	@Override
	public Account getProfile(Account.Type type, String email) {
		return selectRepository(type).findOneByEmail(email).orElseThrow(() -> new EntityNotFoundException("Account isn't exist for email: " + email));
	}

	private AccountRepository<? extends Account> selectRepository(Account.Type type) {
		return switch (type) {
			case EMPLOYEE -> employeeRepository;
			case CLIENT -> clientRepository;
		};
	}
}
