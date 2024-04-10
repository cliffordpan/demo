package me.hchome.example.utils;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * String utils
 *
 * @author Cliff Pan
 */
public final class StringUtils {

	private StringUtils() {
	}

	/**
	 * Joining strings by a delimiter
	 *
	 * @param delimiter joining delimiter, default is ","
	 * @param strings   strings to join
	 */
	public static String joinString(String delimiter, String... strings) {
		if (delimiter == null) {
			delimiter = ",";
		}
		return Stream.<String>of(strings)
				.filter(Objects::nonNull)
				.filter(Predicate.not(String::isBlank))
				.map(String::trim)
				.collect(Collectors.joining(delimiter));
	}

	public static String[] tokenize(String delimiter, String origin) {
		if (origin == null || origin.isBlank()) {
			return null;
		}
		return Stream.of(origin.split(delimiter))
				.filter(Objects::nonNull)
				.filter(Predicate.not(String::isBlank))
				.toArray(String[]::new);
	}
}
