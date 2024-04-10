package me.hchome.example.service.impl;

import jakarta.persistence.EntityNotFoundException;
import me.hchome.example.dao.ProductRepository;
import me.hchome.example.dto.ProductBasic;
import me.hchome.example.model.Product;
import me.hchome.example.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


/**
 * @author Cliff Pan
 * @since
 */
@Service
public class ProductServiceImp implements ProductService {

	private final ProductRepository repository;

	public ProductServiceImp(ProductRepository repository) {
		this.repository = repository;
	}

	@Override
	public Page<ProductBasic> listProducts(String search, Pageable pageable) {
		return this.repository.search(search, pageable);
	}

	@Override
	public Product getProduct(long id) {
		return this.repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
	}

	@Override
	public Product createProduct(Product product) {
		return this.repository.save(product);
	}

	@Override
	public Product updateProduct(long id, Product product) {
		Product dbObject = getProduct(id);
		BeanUtils.copyProperties(product, dbObject, "id", "name");
		return repository.save(dbObject);
	}

	@Override
	public void deleteProduct(long id) {
		repository.deleteById(id);
	}
}
