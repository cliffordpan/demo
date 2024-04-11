package me.hchome.example.controller;

import me.hchome.example.service.DBService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cliff Pan
 * @since
 */
@RestController
@RequestMapping("/api/server")
public class HealthController {

	private final DBService dbService;

	public HealthController(DBService dbService) {
		this.dbService = dbService;
	}

	@GetMapping("/health")
	public String getHealth() {
		return "Status: OK!";
	}

	@GetMapping("/db/reset")
	public void resetDB() {
		dbService.executeReset();
	}

}
