package me.hchome.example.controller;

import jakarta.persistence.EntityNotFoundException;
import me.hchome.example.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

/**
 * @author Cliff Pan
 * @since
 */
public class ProductControllerTests extends AbstractSpringWebTest {

	@Test
	public void test_product_get_list() throws Exception {
		mockMvc.perform(get("/api/products"))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.empty").value(false));
	}

	@Test
	public void test_product_get_not_exists() throws Exception {
		mockMvc.perform(get("/api/products/10"))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(EntityNotFoundException.class.getSimpleName()));
	}

	@Test
	public void test_product_get_exists() throws Exception {
		mockMvc.perform(get("/api/products/1"))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
	}

	@Test
	public void test_product_create() throws Exception {
		String token = getAdminAuth();
		mockMvc.perform(post("/api/products")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(TestUtils.loadFileAsString("tests/Product.json"))
				)
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(5))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test game"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description").value("test game description"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.category").value("RPG"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.platform").value("PC"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.unitPrice").value(10.0));
	}

	@Test
	public void test_product_create_failed() throws Exception {
		String token = getSupportAuth();
		mockMvc.perform(post("/api/products")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(TestUtils.loadFileAsString("tests/Product.json"))
				)
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	public void test_product_delete() throws Exception {
		String token = getAdminAuth();
		mockMvc.perform(delete("/api/products/1")
						.header("Authorization", "Bearer " + token))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isNoContent());
	}

	@Test
	public void test_product_update() throws Exception {
		String token = getAdminAuth();
		mockMvc.perform(put("/api/products/1")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(TestUtils.loadFileAsString("tests/Product.json"))
				)
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Animal Crossing (Digital)"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description").value("test game description"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.category").value("RPG"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.platform").value("PC"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.unitPrice").value(10.0));
	}

}
