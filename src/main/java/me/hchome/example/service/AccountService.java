package me.hchome.example.service;

import me.hchome.example.dto.BaseAccount;
import me.hchome.example.model.Account;

import java.util.List;

/**
 * @author Cliff Pan
 * @since
 */
public interface AccountService {

	List<BaseAccount> listAllBase();

	Account getProfile(Account.Type type, String email);

	boolean isPasswordMatch(Account.Type type, String email, String p);

	Account updatePassword(Account.Type type, String email, String old, String newPassword);

	Account updateAccount(Account.Type type, String email, Account account);
}
