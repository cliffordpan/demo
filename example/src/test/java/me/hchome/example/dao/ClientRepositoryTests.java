package me.hchome.example.dao;

import me.hchome.example.model.Client;
import me.hchome.example.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Cliff Pan
 * @since
 */
public class ClientRepositoryTests extends AbstractSpringRepositoryTest<Client, ClientRepository> {

	@Autowired
	public ClientRepositoryTests(ClientRepository repository) {
		super(repository);
	}

	@Test
	public void test_clients_dao_list_all() {
		List<Client> clients = repository.findAll();
		Assertions.assertEquals(3, clients.size());
	}

	@Test
	public void test_clients_dao_list_enabled() {
		List<Client> clients = repository.findAllByEnabledTrue();
		Assertions.assertEquals(2, clients.size());
	}

	@ParameterizedTest
	@MethodSource("test_clients_dao_get_by_email_source")
	public void test_clients_dao_get_by_email(String email, boolean expect) {
		Optional<Client> clientOpt = repository.findOneByEmail(email);
		Assertions.assertEquals(expect, clientOpt.isPresent());
	}

	@Test
	public void test_clients_dao_delete() {
		repository.deleteById(3L);
		Assertions.assertFalse(repository.findOneByEmail("client1@hchome.me").isPresent());
	}

	@Test
	public void test_clients_dao_save() throws Exception {
		Client client = repository.save(TestUtils.loadObject(Client.class, "tests/Client.json"));
		Assertions.assertNotEquals(0, client.getId());
		Assertions.assertEquals("client4@hchome.me", client.getEmail());
		Assertions.assertEquals("Client4", client.getFirstName());
		Assertions.assertEquals("Test", client.getLastName());
		Assertions.assertTrue(client.isEnabled());
	}

	private static Stream<Arguments> test_clients_dao_get_by_email_source() {
		return Stream.of(
				Arguments.of("client1@hchome.me", true),
				Arguments.of("admin@hchome.me", false)
		);
	}
}
