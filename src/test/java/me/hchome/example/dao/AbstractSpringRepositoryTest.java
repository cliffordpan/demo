package me.hchome.example.dao;

import me.hchome.example.AbstractSpringTest;
import me.hchome.example.model.DomainEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

/**
 * @author Cliff Pan
 * @since
 */
public abstract class AbstractSpringRepositoryTest<T extends DomainEntity, R extends JpaRepository<T, Long>> extends AbstractSpringTest {
	protected final R repository;
	public AbstractSpringRepositoryTest(R repository) {
		this.repository = repository;
	}

	@Test
	public void test_context_load() {
		Assertions.assertNotNull(repository);
	}
}
