package me.hchome.example.exception;

/**
 * @author Cliff Pan
 * @since
 */
public class ServiceNotAvailable extends RuntimeException {

	public ServiceNotAvailable() {
		super("Service not available at this moment");
	}
}
