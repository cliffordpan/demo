package me.hchome.example.service;

import me.hchome.example.model.Account;

/**
 * @author Cliff Pan
 * @since
 */
public interface AccountService {
	Account getProfile(Account.Type type, String email);

	boolean isPasswordMatch(Account.Type type, String email, String p);

	Account updatePassword(Account.Type type, String email, String old, String newPassword);

	Account updateAccount(Account.Type type, String email, Account account);
}
