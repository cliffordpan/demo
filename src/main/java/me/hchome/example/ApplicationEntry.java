package me.hchome.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Application Entrypoint. This is a simple online game store - backend.
 * configurations in package {@link me.hchome.example.config}
 *
 * @author Cliff Pan
 */
@SuppressWarnings("PMD")
@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing(auditorAwareRef = "auditingAware")
@EnableScheduling
public class ApplicationEntry {

    /**
     * main entry point.
     *
     * @param args cmd line params
     */
    public static void main(final String[] args) {
        SpringApplication.run(ApplicationEntry.class, args);
    }

}
