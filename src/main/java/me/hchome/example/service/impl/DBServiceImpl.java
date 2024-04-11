package me.hchome.example.service.impl;

import me.hchome.example.service.DBService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * @author Cliff Pan
 * @since
 */
@Service
class DBServiceImpl implements DBService {

	private static final Logger LOG = LoggerFactory.getLogger(DBServiceImpl.class);
	private final DataSource dataSource;
	private final ApplicationAvailability availability;
	private final ApplicationEventPublisher eventPublisher;

	public DBServiceImpl(DataSource dataSource, ApplicationAvailability availability, ApplicationEventPublisher eventPublisher) {
		this.dataSource = dataSource;
		this.availability = availability;
		this.eventPublisher = eventPublisher;
	}

	/**
	 * Schedule every date reset db
	 */
	@Scheduled(cron = "0 0 0 * * ?")
	@Override
	public void executeReset() {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScripts(new ClassPathResource("clean.sql"), new ClassPathResource("data.sql"));
		try {
			LOG.info("Starting reset database, enter maintenance mode");
			AvailabilityChangeEvent.publish(eventPublisher, this, ReadinessState.REFUSING_TRAFFIC);
			populator.execute(dataSource);
			LOG.info("Done reset database, accept traffic");
		} catch (ScriptException ex) {
			LOG.warn("Failed to reset db, accept traffic", ex);
			throw ex;
		} finally {
			AvailabilityChangeEvent.publish(eventPublisher, this, ReadinessState.ACCEPTING_TRAFFIC);
		}
	}

}
