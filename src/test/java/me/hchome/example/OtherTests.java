package me.hchome.example;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Cliff Pan
 * @since
 */
public class OtherTests {

	@Test
	public void test() {
		System.err.println(String.join(", ", "A", null, "B"));
		System.err.println(Stream.<String>of(null, null, null).filter(Objects::nonNull).collect(Collectors.joining(", ")));
	}

	@Test
	public void password_gen() {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

		System.err.println(encoder.encode("psw123"));
	}
}
