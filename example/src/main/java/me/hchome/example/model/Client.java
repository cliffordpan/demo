package me.hchome.example.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;


/**
 * Client entity
 *
 * @author Cliff Pan
 */
@Entity
@DiscriminatorValue(Account.CLIENT)
public class Client extends Account {

	@Size(max = 32)
	@Column(name = "nick_name", length = 32)
	private String nickName;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@Transient
	public Address getBillingAddress() {
		return getAddresses().get(Address.AddressType.BILLING);
	}

	public void setBillingAddress(Address address) {
		getAddresses().put(Address.AddressType.BILLING, address);
	}


	@Transient
	public Address getShippingAddress() {
		return getAddresses().get(Address.AddressType.SHIPPING);
	}

	public void setShippingAddress(Address address) {
		getAddresses().put(Address.AddressType.SHIPPING, address);
	}

	@Override
	boolean isAccessible() {
		return true;
	}
}
