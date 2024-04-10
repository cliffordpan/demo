package me.hchome.example.dao;

import me.hchome.example.dto.ProductBasic;
import me.hchome.example.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Cliff Pan
 * @since
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

	@Query(name = "Product.searchProducts", countName = "Product.searchProductsCount")
	Page<ProductBasic> search(@Param("search") String criteria, Pageable pageable);
}
