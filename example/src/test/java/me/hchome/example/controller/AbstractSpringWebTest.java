package me.hchome.example.controller;

import me.hchome.example.AbstractSpringTest;
import me.hchome.example.dto.Tokens;
import me.hchome.example.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Cliff Pan
 * @since
 */
@WebAppConfiguration
public class AbstractSpringWebTest extends AbstractSpringTest {

	public final static String ADMIN_BASIC_AUTH = "Basic YWRtaW5AaGNob21lLm1lOnBzdzEyMw==";
	public final static String SUPPORT_BASIC_AUTH = "Basic c3VwcG9ydEBoY2hvbWUubWU6cHN3MTIz";
	public final static String CLIENT_1_BASIC_AUTH = "Basic Y2xpZW50MUBoY2hvbWUubWU6cHN3MTIz";
	public final static String CLIENT_2_BASIC_AUTH = "Basic Y2xpZW50MkBoY2hvbWUubWU6cHN3MTIz";

	@Autowired
	private WebApplicationContext webApplicationContext;

	protected MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		this.mockMvc = MockMvcBuilders
				.webAppContextSetup(this.webApplicationContext)
				.apply(SecurityMockMvcConfigurers.springSecurity())
				.build();
	}


	protected String getAdminAuth() throws Exception {
		return getAuthToken(ADMIN_BASIC_AUTH);
	}

	protected String getSupportAuth() throws Exception {
		return getAuthToken(SUPPORT_BASIC_AUTH);
	}

	protected String getClient1Auth() throws Exception {
		return getAuthToken(CLIENT_1_BASIC_AUTH);
	}

	protected String getClient2Auth() throws Exception {
		return getAuthToken(CLIENT_2_BASIC_AUTH);
	}

	public String getAuthToken(String basicToken) throws Exception {
		byte[] content = mockMvc.perform(MockMvcRequestBuilders.post("/auth/token")
				.header("Authorization", basicToken)
		).andReturn().getResponse().getContentAsByteArray();
		Tokens tokens = TestUtils.readObject(Tokens.class, content);
		return tokens.accessToken();
	}
}
