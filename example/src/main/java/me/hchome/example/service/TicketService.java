package me.hchome.example.service;

import me.hchome.example.dto.AccountDetails;
import me.hchome.example.model.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Cliff Pan
 * @since
 */
public interface TicketService {
	Ticket createTicket(Ticket ticket);

	Page<Ticket> listTickets(boolean showClosed, Pageable pageable);

	Ticket getTicket(long id);

	Ticket assignTicket(long id, long employeeId);

	Ticket closeTicket(long id);

	Ticket updateTicket(long id, Ticket ticket);
}
