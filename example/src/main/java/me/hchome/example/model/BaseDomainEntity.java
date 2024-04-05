package me.hchome.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;


/**
 * @author Cliff Pan
 * @since
 */
@MappedSuperclass
abstract class BaseDomainEntity implements DomainEntity {

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private long id;

	@Override
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
