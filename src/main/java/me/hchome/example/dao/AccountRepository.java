package me.hchome.example.dao;

import me.hchome.example.dto.BaseAccount;
import me.hchome.example.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Cliff Pan
 * @since
 */
@Repository
public interface AccountRepository<T extends Account> extends JpaRepository<T, Long> {

	@Query("select a from Account a")
	List<BaseAccount> findAllBase();

	/**
	 * Find one by its email/username
	 */
	Optional<T> findOneByEmail(String email);

	/**
	 * Find all by an account is enabled
	 */
	List<T> findAllByEnabledTrue();
}
