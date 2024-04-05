package me.hchome.example.service;

import me.hchome.example.dto.ProductBasic;
import me.hchome.example.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Cliff Pan
 * @since
 */
public interface ProductService {
	Page<ProductBasic> listProducts(String search, Pageable pageable);

	Product getProduct(long id);

	Product createProduct(Product product);

	Product updateProduct(long id, Product product);

	void deleteProduct(long id);
}
