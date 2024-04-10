package me.hchome.example.service;

import me.hchome.example.model.Account;

/**
 * @author Cliff Pan
 * @since
 */
public interface AccountService {
	Account getProfile(Account.Type type, String email);
}
