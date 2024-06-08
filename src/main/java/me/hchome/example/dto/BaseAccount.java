package me.hchome.example.dto;

import me.hchome.example.model.Account;

/**
 * @author Cliff Pan
 * @since
 */
public interface BaseAccount {
	long getId();

	Account.Type getType();
	String getFirstName();
	String getLastName();
}
