package me.hchome.example.dao;

import me.hchome.example.model.Ticket;
import me.hchome.example.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Cliff Pan
 * @since
 */
public class TicketRepositoryTests extends AbstractSpringRepositoryTest<Ticket, TicketRepository> {

	@Autowired
	public TicketRepositoryTests(TicketRepository repository) {
		super(repository);
	}

	@Test
	public void test_product_dao_create() throws Exception {
		Ticket ticket = repository.save(TestUtils.loadObject(Ticket.class, "tests/Ticket.json"));
		Assertions.assertNotNull(ticket);
		Assertions.assertEquals("test title", ticket.getTitle());
		Assertions.assertEquals("test description", ticket.getDescription());
	}

	@Test
	public void test_product_dao_delete() throws Exception {
		Ticket ticket = repository.save(TestUtils.loadObject(Ticket.class, "tests/Ticket.json"));
		long ticketId = ticket.getId();
		repository.deleteById(ticketId);
		Assertions.assertFalse(repository.findById(ticketId).isPresent());
	}

}
