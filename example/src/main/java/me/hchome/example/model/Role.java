package me.hchome.example.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Administrative Roles for employees only
 *
 * @author Cliff Pan
 */
public enum Role {
	ADMIN, SUPPORT, CLIENT;

	public GrantedAuthority asAuthority() {
		return new SimpleGrantedAuthority("ROLE_" + this.name());
	}
}
