package me.hchome.example.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Employees entity
 *
 * @author Cliff Pan
 */
@NamedEntityGraphs({
		@NamedEntityGraph(name = "Employee.withRoles", attributeNodes = @NamedAttributeNode("roles"))
})
@Entity
@DiscriminatorValue(Account.EMPLOYEE)
@JsonTypeName(Account.EMPLOYEE)
public class Employee extends Account {

	@ElementCollection
	@CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "acc_id",
			foreignKey = @ForeignKey(name = "accounts_roles_fk")))
	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private Set<Role> roles = new HashSet<>();

	@Transient
	public Address getMailingAddress() {
		return getAddresses().get(Address.AddressType.MAILING);
	}

	public void setMailingAddress(Address address) {
		getAddresses().put(Address.AddressType.MAILING, address);
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	boolean isAccessible() {
		return true;
	}
}
