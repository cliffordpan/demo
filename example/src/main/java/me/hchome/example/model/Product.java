package me.hchome.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.hibernate.jpa.AvailableHints;

import java.math.BigDecimal;

/**
 * @author Cliff Pan
 * @since
 */
@NamedQueries({
		@NamedQuery(name = "Product.searchProducts", query = "select p from Product p where " +
				"lower(cast(p.name as string)) like lower(concat('%', cast(:search as string), '%')) or " +
				"lower(cast(p.description as string)) like lower(concat('%', cast(:search as string), '%')) or " +
				"lower(cast(p.category as string)) like lower(concat('%', cast(:search as string), '%'))",
				hints = @QueryHint(name = AvailableHints.HINT_READ_ONLY, value = "true")),
		@NamedQuery(name = "Product.searchProductsCount", query = "select count(p.id) from Product p where " +
				"lower(cast(p.name as string)) like lower(concat('%', cast(:search as string), '%')) or " +
				"lower(cast(p.description as string)) like lower(concat('%', cast(:search as string), '%')) or " +
				"lower(cast(p.category as string)) like lower(concat('%', cast(:search as string), '%'))",
				hints = @QueryHint(name = AvailableHints.HINT_READ_ONLY, value = "true"))
})
@Entity
@Table(name = "products")
public class Product extends BaseDomainEntity {

	@Size(max = 128)
	@Column(length = 128, updatable = false)
	private String name;

	@Basic(fetch = FetchType.EAGER)
	@Lob
	@Column(columnDefinition = "TEXT DEFAULT NULL")
	private String description;
	@Column(name = "unit_price", scale = 2, precision = 10, nullable = false)
	@PositiveOrZero
	private BigDecimal unitPrice;

	@Size(max = 64)
	@Column(length = 64)
	private String category;

	@Enumerated(EnumType.STRING)
	private Platform platform;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public enum Platform {
		PS4, PS5, XBOX, SWITCH, PC;
	}
}
