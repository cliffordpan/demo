package me.hchome.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * All accounts share this common superclass, which is inherited by customers, employees, and suppliers.
 *
 * @author Cliff Pan
 */
@Entity
@Table(name= "accounts", uniqueConstraints = @UniqueConstraint(name = "accounts_email_uk", columnNames = "email"))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", length = 16, discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(name = Account.CLIENT, value = Client.class),
		@JsonSubTypes.Type(name = Account.EMPLOYEE, value = Employee.class)
})
public abstract class Account extends BaseDomainEntity {

	public final static String CLIENT = "CLIENT";
	public final static String EMPLOYEE = "EMPLOYEE";

	/**
	 * email as username, non updatable
	 */
	@NotBlank
	@Size(max = 64)
	@Email
	@Column(updatable = false, length = 64, nullable = false)
	private String email;

	/**
	 * use spring default password encoder (Bcrypt(9))
	 */
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Size(max = 84)
	@Column(length = 84)
	private String password;

	private boolean enabled = true;

	@CreationTimestamp
	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@NotBlank
	@Column(name = "first_name", length = 32, nullable = false)
	private String firstName;

	@NotBlank
	@Column(name = "last_name", length = 32, nullable = false)
	private String lastName;

	@ElementCollection
	@CollectionTable(name = "account_addresses",
			joinColumns = @JoinColumn(name = "acc_id", foreignKey = @ForeignKey(name = "accounts_addresses_fk")))
	@MapKeyColumn(name = "type", length = 10)
	@MapKeyEnumerated(EnumType.STRING)
	private Map<Address.AddressType, Address> addresses = new HashMap<>();

	@Column(insertable = false, updatable = false)
	@Enumerated(EnumType.STRING)
	private Type type;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	@JsonIgnore
	public Map<Address.AddressType, Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Map<Address.AddressType, Address> addresses) {
		this.addresses = addresses;
	}

	@Transient
	abstract boolean isAccessible();

	public String getPassword() {
		return password;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public enum Type {
		CLIENT, EMPLOYEE
	}
}
