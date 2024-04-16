package me.hchome.example.controller;

import jakarta.validation.constraints.NotNull;
import me.hchome.example.dto.AccountDetails;
import me.hchome.example.model.Account;
import me.hchome.example.service.AccountService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

	@PostMapping("/testPassword")
	public boolean testPassword(@AuthenticationPrincipal AccountDetails details, @RequestBody @NotNull UpdatePassword dto) {
		return service.isPasswordMatch(details.getType(), details.getEmail(), dto.getPassword());
	}


	@PostMapping("/updatePassword")
	public Account updatePassword(@AuthenticationPrincipal AccountDetails details, @RequestBody @NotNull UpdatePassword dto) {
		return service.updatePassword(details.getType(), details.getEmail(), dto.getOld(), dto.getPassword());
	}

	@PatchMapping("/updateAccount")
	public Account updateAccount(@AuthenticationPrincipal AccountDetails details, @RequestBody @NotNull Account dto) {
		return service.updateAccount(details.getType(), details.getEmail(), dto);
	}

	static class UpdatePassword {
		private String old;
		private String password;

		public String getOld() {
			return old;
		}

		public void setOld(String old) {
			this.old = old;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}
}
