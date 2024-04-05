package me.hchome.example.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Table;
import org.hibernate.annotations.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

/**
 * @author Cliff Pan
 * @since
 */

@Table(name = "tickets")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Ticket extends BaseDomainEntity {

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@CreatedBy
	@JoinColumn(name = "cid", foreignKey = @ForeignKey(name = "accounts_tickets_cid_fk"))
	@JsonIdentityReference(alwaysAsId = true)
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Client client;

	@ManyToOne
	@JoinColumn(name = "eid", foreignKey = @ForeignKey(name = "accounts_tickets_eid_fk"))
	@OnDelete(action = OnDeleteAction.SET_NULL)
	@JsonIdentityReference(alwaysAsId = true)
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Employee assigned;

	@LastModifiedBy
	@Any
	@AnyKeyJavaClass(Long.class)
	@AnyDiscriminatorValues({
			@AnyDiscriminatorValue(discriminator = Account.CLIENT, entity = Client.class),
			@AnyDiscriminatorValue(discriminator = Account.EMPLOYEE, entity = Employee.class)
	})
	@OnDelete(action = OnDeleteAction.SET_NULL)
	@Column(name = "modified_type", length = 10)
	@JoinColumn(name = "modified_by", foreignKey = @ForeignKey(name = "accounts_tickets_modified_by_fk"))
	@JsonIdentityReference(alwaysAsId = true)
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Account modifiedBy;

	@Column(length = 128)
	private String title;

	@Lob
	@Column(columnDefinition = "text default null")
	private String description;

	@CreationTimestamp
	@Column(name = "created_date")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Instant createdDate;

	@UpdateTimestamp
	@Column(name = "updated_date")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Instant updatedDate;

	@Column(name = "closed_date")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Instant closedDate;


	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Employee getAssigned() {
		return assigned;
	}

	public void setAssigned(Employee assigned) {
		this.assigned = assigned;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Instant getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Instant createdDate) {
		this.createdDate = createdDate;
	}

	public Instant getClosedDate() {
		return closedDate;
	}

	public void setClosedDate(Instant closedDate) {
		this.closedDate = closedDate;
	}

	public Account getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Account modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Instant getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Instant updatedDate) {
		this.updatedDate = updatedDate;
	}

}
