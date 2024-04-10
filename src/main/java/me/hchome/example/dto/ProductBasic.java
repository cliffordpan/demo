package me.hchome.example.dto;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

/**
 * @author Cliff Pan
 * @since
 */
public interface ProductBasic {
	long getId();
	String getName();
	String getDescription();
	String getCategory();
	@Value("#{target.unitPrice}")
	BigDecimal getPrice();
}
