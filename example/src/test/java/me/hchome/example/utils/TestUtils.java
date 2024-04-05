package me.hchome.example.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * @author Cliff Pan
 */
public final class TestUtils {

	private final static ObjectMapper MAPPER = new ObjectMapper();

	private TestUtils() {
	}

	public static <T> T loadObject(Class<T> clazz, @Nonnull String file) throws IOException {
		ClassPathResource resource = new ClassPathResource(file);
		return MAPPER.readValue(resource.getInputStream(), clazz);
	}

	public static String loadFileAsString(@Nonnull String file) throws IOException {
		ClassPathResource resource = new ClassPathResource(file);
		try (InputStream is = resource.getInputStream(); BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
			return reader.lines().collect(Collectors.joining(System.lineSeparator()));
		}
	}

	public static <T> T readObject(Class<T> clazz, byte[] data) throws Exception {
		return MAPPER.readValue(data, clazz);
	}

	public static <T> T readObject(TypeReference<T> reference, byte[] data) throws Exception {
		return MAPPER.readValue(data, reference);
	}
}
