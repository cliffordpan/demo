package me.hchome.example.controller;

import me.hchome.example.dao.TicketRepository;
import me.hchome.example.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * @author Cliff Pan
 * @since
 */
public class TicketControllerTest extends AbstractSpringWebTest {

	@Autowired
	TicketRepository repository;

	@Test
	public void test_tick_controller_create() throws Exception {
		String token = getClient1Auth();
		mockMvc.perform(post("/api/tickets")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(TestUtils.loadFileAsString("tests/Ticket.json")))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(5))
				.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("test title"));
	}


	@Test
	public void test_tick_controller_create_failed() throws Exception {
		// Employee isn't allowed to create tickets
		String token = getAdminAuth();
		mockMvc.perform(post("/api/tickets")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(TestUtils.loadFileAsString("tests/Ticket.json")))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}


	@ParameterizedTest
	@MethodSource("test_ticket_controller_list_data_source")
	public void test_tick_controller_list(String basic, int expectSize) throws Exception {
		String token = getAuthToken(basic);
		mockMvc.perform(get("/api/tickets")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value(expectSize));
	}

	@Test
	public void test_ticket_controller_assign() throws Exception {
		// Employee isn't allowed to create tickets
		String token = getAdminAuth();
		mockMvc.perform(post("/api/tickets/1")
						.queryParam("employeeId", "2")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(TestUtils.loadFileAsString("tests/Ticket.json")))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.assigned").value(2));
	}

	@ParameterizedTest
	@CsvSource({
			"1,404",
			"2,200",
			"3,200",
			"4,404"
	})
	public void test_ticket_controller_get_by_id(int id, int status) throws Exception {
		String token = getSupportAuth();
		mockMvc.perform(get("/api/tickets/" + id)
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().is(status));
	}

	private static Stream<Arguments> test_ticket_controller_list_data_source() {
		return Stream.of(
				Arguments.of(AbstractSpringWebTest.ADMIN_BASIC_AUTH, 4),
				Arguments.of(AbstractSpringWebTest.SUPPORT_BASIC_AUTH, 2),
				Arguments.of(AbstractSpringWebTest.CLIENT_1_BASIC_AUTH, 3),
				Arguments.of(AbstractSpringWebTest.CLIENT_2_BASIC_AUTH, 1)
		);
	}


}
