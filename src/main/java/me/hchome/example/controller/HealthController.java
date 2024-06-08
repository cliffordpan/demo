package me.hchome.example.controller;

import jakarta.persistence.EntityNotFoundException;
import me.hchome.example.service.DBService;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

/**
 * @author Cliff Pan
 * @since
 */
@RestController
@RequestMapping("/api/server")
public class HealthController {

	private final DBService dbService;

	private final ApplicationAvailability availability;

	public HealthController(DBService dbService, ApplicationAvailability availability) {
		this.dbService = dbService;
		this.availability = availability;
	}

	@GetMapping("/health")
	public Callable<String> getHealth() {
		return () -> "Status: OK!";
	}

	@GetMapping("/status")
	public Callable<ResponseEntity<String>> getStatus() {
		return () -> {
			String status = switch (availability.getReadinessState()) {
				case REFUSING_TRAFFIC -> "REFUSE";
				case ACCEPTING_TRAFFIC -> "ACCEPT";
			};
			return ResponseEntity.ok(status);
		};
	}

	@GetMapping("/db/reset")
	public Callable<Void> resetDB() {
		return () -> {
			dbService.executeReset();
			return null;
		};
	}

}
