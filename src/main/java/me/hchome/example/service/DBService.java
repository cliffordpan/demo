package me.hchome.example.service;

/**
 * Service for database operations
 *
 * @author Cliff Pan
 * @since
 */
public interface DBService {

	/**
	 * Reset database, all tables will reset by a sql script.
	 */
	void executeReset();
}
