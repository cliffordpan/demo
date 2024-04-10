package me.hchome.example.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import me.hchome.example.dao.EmployeeRepository;
import me.hchome.example.dao.TicketRepository;
import me.hchome.example.dto.AccountDetails;
import me.hchome.example.model.Client;
import me.hchome.example.model.Employee;
import me.hchome.example.model.Role;
import me.hchome.example.model.Ticket;
import me.hchome.example.service.TicketService;
import me.hchome.example.utils.AccountDetailsUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Cliff Pan
 * @since
 */
@Service
public class TicketServiceImpl implements TicketService {

	private final TicketRepository repository;
	private final EmployeeRepository employeeRepository;

	public TicketServiceImpl(TicketRepository repository, EmployeeRepository employeeRepository) {
		this.repository = repository;
		this.employeeRepository = employeeRepository;
	}

	@Override
	public Ticket createTicket(Ticket ticket) {
		return this.repository.save(ticket);
	}

	@Override
	public Page<Ticket> listTickets(boolean showClosed, Pageable pageable) {
		AccountDetails details = getDetails();
		Set<Role> roles = details.getAuthorities().stream().map(grant -> Role.valueOf(grant.toString().substring("ROLE_".length())))
				.collect(Collectors.toSet());
		Specification<Ticket> accountSpec = Specification.where((root, query, cb) -> switch (details.getType()) {
			case CLIENT -> {
				Join<Ticket, Client> client = root.join("client");
				yield cb.and(cb.isNotNull(client), cb.equal(client.get("id"), cb.literal(details.getId())));
			}
			case EMPLOYEE -> {
				if (roles.contains(Role.ADMIN)) {
					yield cb.conjunction();
				} else if (roles.contains(Role.SUPPORT)) {
					Join<Ticket, Employee> emp = root.join("assigned");
					yield cb.and(cb.isNotNull(emp), cb.equal(emp.get("id"), cb.literal(details.getId())));
				} else {
					yield cb.disjunction();
				}
			}
		});

		return this.repository.findAll(accountSpec.and((root, query, cb) -> showClosed ? cb.conjunction() : cb.isNull(root.get("closedDate"))),
				pageable);
	}

	@Override
	public Ticket getTicket(long id) {
		AccountDetails details = getDetails();
		return switch (details.getType()) {
			case EMPLOYEE -> {
				if (details.getAuthorities().contains(Role.ADMIN.asAuthority())) {
					yield getTicketWithoutRoles(id);
				} else if (details.getAuthorities().contains(Role.SUPPORT.asAuthority())) {
					yield repository.findOne(bySupportRole(details).and(byId(id)))
							.orElseThrow(() -> new EntityNotFoundException("Ticket not found or forbidden to access"));
				}
				throw new EntityNotFoundException("Ticket not found or forbidden to access");
			}
			case CLIENT -> repository.findOne(byClientRole(details).and(byId(id)))
					.orElseThrow(() -> new EntityNotFoundException("Ticket not found or forbidden to access"));

		};
	}

	@Override
	public Ticket assignTicket(long id, long employeeId) {
		Ticket ticket = getTicketWithoutRoles(id);
		Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EntityNotFoundException("Employee not found"));
		ticket.setAssigned(employee);
		return repository.save(ticket);
	}

	@Override
	public Ticket closeTicket(long id) {
		Ticket ticket = getTicket(id);
		ticket.setClosedDate(Instant.now());
		return repository.save(ticket);
	}

	@Override
	public Ticket updateTicket(long id, Ticket ticket) {
		Ticket dbObject = getTicket(id);
		BeanUtils.copyProperties(ticket, dbObject, "id", "title", "assigned", "client",
				"modifiedBy", "createdDate", "updatedDate", "closedDate");
		return repository.save(dbObject);
	}

	private AccountDetails getDetails() {
		return AccountDetailsUtils.getAccountDetails()
				.orElseThrow(() -> new InsufficientAuthenticationException("Missing account details"));
	}

	private Ticket getTicketWithoutRoles(long id) {
		return this.repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
	}

	private static Specification<Ticket> byId(long id) {
		return Specification.where((root, query, cb) -> cb.equal(root.get("id"), id));
	}

	private static Specification<Ticket> bySupportRole(AccountDetails details) {
		return Specification.where((root, query, cb) -> cb.and(
				cb.isNotNull(root.get("assigned")),
				cb.equal(root.get("assigned").get("id"), details.getId())
		));
	}

	private static Specification<Ticket> byClientRole(AccountDetails details) {
		return Specification.where((root, query, cb) -> cb.and(
				cb.isNotNull(root.get("client")),
				cb.equal(root.get("client").get("id"), details.getId())
		));
	}
}
