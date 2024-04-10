package me.hchome.example.dao;

import me.hchome.example.AbstractSpringTest;
import me.hchome.example.model.Product;
import me.hchome.example.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Cliff Pan
 * @since
 */

public class ProductRepositoryTests extends AbstractSpringRepositoryTest<Product, ProductRepository> {

	@Autowired
	public ProductRepositoryTests(ProductRepository repository) {
		super(repository);
	}

	@Test
	public void test_product_dao_create() throws Exception {
		Product product = repository.save(TestUtils.loadObject(Product.class, "tests/Product.json"));
		Assertions.assertNotNull(product);
		Assertions.assertTrue(product.getId() > 0);
		Assertions.assertEquals("test game", product.getName());
		Assertions.assertEquals("test game description", product.getDescription());
		Assertions.assertEquals("RPG", product.getCategory());
		Assertions.assertEquals(Product.Platform.PC, product.getPlatform());
		Assertions.assertEquals(new BigDecimal("10.0"), product.getUnitPrice());
	}

	@Test
	public void test_product_dao_delete() throws Exception {
		repository.deleteById(1L);
		Assertions.assertFalse(repository.findById(1L).isPresent());
	}

	@Test
	public void test_product_dao_list() throws Exception {
		List<Product> products = repository.findAll();
		Assertions.assertEquals(4, products.size());
	}
}
