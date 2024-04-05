package me.hchome.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import me.hchome.example.utils.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Cliff Pan
 * @since
 */
@Embeddable
public class Address implements Serializable {

	public enum AddressType {
		BILLING, SHIPPING, MAILING
	}

	@NotBlank
	@Size(max = 128)
	@Column(length = 128)
	private String line1;
	@Column(length = 128)
	private String line2;

	@Size(max = 8)
	@Column(length = 8)
	private String unit;

	@Size(max = 64)
	@Column(length = 64)
	private String city;

	private String region;

	/**
	 * ISO Country Code
	 */
	@Size(max = 2)
	@Column(length = 2, nullable = false)
	private String country;

	@Column(length = 10, nullable = false)
	private String postal;

	@Transient
	private String getStreet() {
		return StringUtils.joinString(", ", line1, line2, unit);
	}

	public String getLine1() {
		return line1;
	}

	public void setLine1(String line1) {
		this.line1 = line1;
	}

	public String getLine2() {
		return line2;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPostal() {
		return postal;
	}

	public void setPostal(String postal) {
		this.postal = postal;
	}
}
