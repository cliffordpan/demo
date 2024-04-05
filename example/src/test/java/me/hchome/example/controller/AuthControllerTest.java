package me.hchome.example.controller;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

/**
 * @author Cliff Pan
 * @since
 */
public class AuthControllerTest extends AbstractSpringWebTest {

	@Test
	public void test_auth_get_token_success() throws Exception {
		mockMvc.perform(post("/auth/token")
						.header("Authorization", "Basic YWRtaW5AaGNob21lLm1lOnBzdzEyMw=="))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").hasJsonPath())
				.andExpect(MockMvcResultMatchers.cookie().exists("refresh_token"));
	}

	@Test
	public void test_auth_get_token_failed_username_not_found() throws Exception {
		mockMvc.perform(post("/auth/token")
						.header("Authorization", "Basic YWRtaUBoY2hvbWUubWU6cHN3MTIz"))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	@Test
	public void test_auth_get_token_failed_account_disabled() throws Exception {
		mockMvc.perform(post("/auth/token")
						.header("Authorization", "Basic Y2xpZW50M0BoY2hvbWUubWU6cHN3MTIz"))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}


	@Test
	public void test_auth_refresh_token_success() throws Exception {
		MvcResult result = mockMvc.perform(post("/auth/token")
						.header("Authorization", "Basic YWRtaW5AaGNob21lLm1lOnBzdzEyMw=="))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.cookie().exists("refresh_token"))
				.andReturn();
		Cookie refreshToken = Objects.requireNonNull(result.getResponse().getCookie("refresh_token"));

		mockMvc.perform(post("/auth/refresh")
						.cookie(refreshToken))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").hasJsonPath());
	}

}
