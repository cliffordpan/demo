package me.hchome.example.controller;

import jakarta.validation.Valid;
import me.hchome.example.dto.ProductBasic;
import me.hchome.example.model.Product;
import me.hchome.example.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Cliff Pan
 * @since
 */
@RestController
@RequestMapping("/api/products")
@Valid
public class ProductController {

	private final ProductService service;

	public ProductController(ProductService service) {
		this.service = service;
	}

	@GetMapping
	public Page<ProductBasic> list(@RequestParam(defaultValue = "") String search, @PageableDefault Pageable pageable) {
		return service.listProducts(search, pageable);
	}

	@GetMapping("/{id:\\d+}")
	public Product get(@PathVariable long id) {
		return service.getProduct(id);
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public Product saveProduct(@RequestBody @Validated Product product) {
		return service.createProduct(product);
	}

	@PutMapping("/{id:\\d+}")
	@PreAuthorize("hasRole('ADMIN')")
	public Product updateProduct(@PathVariable long id, @RequestBody @Validated Product product) {
		return service.updateProduct(id, product);
	}

	@DeleteMapping("/{id:\\d+}")
	@PreAuthorize("hasRole('ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteProduct(@PathVariable long id) {
		service.deleteProduct(id);
	}
}
