package me.hchome.example.controller;

import jakarta.validation.Valid;
import me.hchome.example.dto.AccountDetails;
import me.hchome.example.model.Ticket;
import me.hchome.example.service.TicketService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


/**
 * @author Cliff Pan
 * @since
 */
@RestController
@RequestMapping("/api/tickets")
@Valid
public class TicketController {

	private final TicketService service;

	public TicketController(TicketService service) {
		this.service = service;
	}

	@PreAuthorize("hasRole('CLIENT')")
	@PostMapping
	public Ticket createTicket(@RequestBody @Validated Ticket ticket) {
		return service.createTicket(ticket);
	}

	@GetMapping
	public Page<Ticket> listTickets(@RequestParam(defaultValue = "false") boolean includeClosed, Pageable pageable) {
		return service.listTickets(includeClosed, pageable);
	}

	@GetMapping("/{id:\\d+}")
	public Ticket getTicket(@PathVariable long id) {
		return service.getTicket(id);
	}

	@PutMapping("/{id:\\d+}")
	public Ticket updateTicket(@PathVariable long id, @RequestBody @Validated Ticket ticket) {
		return service.updateTicket(id, ticket);
	}


	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "/{id:\\d+}", params = "employeeId")
	public Ticket assignTicket(@PathVariable long id, @RequestParam long employeeId) {
		return service.assignTicket(id, employeeId);
	}

	@PostMapping("/{id:\\d+}/close")
	public Ticket closeTicket(@PathVariable long id) {
		return service.closeTicket(id);
	}
}
