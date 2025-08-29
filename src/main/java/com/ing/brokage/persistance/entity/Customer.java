package com.ing.brokage.persistance.entity;

import com.ing.brokage.persistance.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "CUSTOMER")
@Audited
@AuditTable(value = "AU_CUSTOMER")
@SequenceGenerator(name = "idgen", sequenceName = "CUSTOMER_SEQ", allocationSize = 1)
public class Customer extends BaseEntity implements UserDetails {

    @Column(nullable = false, length = 50)
	String name;

    @Column(nullable = false, length = 50)
	String surname;

	@Column(nullable = false, unique = true, length = 100)
	String email;

    @Column(nullable = false, length = 50)
	String username;

    @Column(nullable = false, length = 50)
	String password;

	@Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
	private UserRoleEnum role;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(() -> "ROLE_" + role); // ROLE_ADMIN-ROLE_USER
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
