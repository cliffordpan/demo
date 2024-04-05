package me.hchome.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Application Entrypoint. This is a simple online game store - backend.
 *
 * configurations in package {@link me.hchome.example.config}
 *
 * @author Cliff Pan
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing(auditorAwareRef = "auditingAware")
public class AngularGameStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(AngularGameStoreApplication.class, args);
	}

}
