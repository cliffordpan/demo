package me.hchome.example.controller;

import me.hchome.example.dto.AccountDetails;
import me.hchome.example.model.Account;
import me.hchome.example.service.AccountService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cliff Pan
 */
@RestController
@RequestMapping("/api/self")
public class SelfController {

	private final AccountService service;

	public SelfController(AccountService service) {
		this.service = service;
	}

	@GetMapping
	public Account self(@AuthenticationPrincipal AccountDetails details) {
		return service.getProfile(details.getType(), details.getEmail());
	}
}
