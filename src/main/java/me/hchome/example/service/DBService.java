package me.hchome.example.service;

/**
 * Service for database operations
 *
 * @author Cliff Pan
 * @since
 */
public interface DBService {

	/**
	 * Reset database, all will reset by script
	 */
	void executeReset();
}
