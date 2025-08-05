package com.urbanproperty.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class UserEntity extends BaseEntity{
	 @Column(name = "first_name", length = 50, nullable = false)
	    private String firstName;

	    @Column(name = "last_name", length = 50, nullable = false)
	    private String lastName;

	    @Column(name = "email", length = 100, nullable = false, unique = true)
	    private String email;

	    @Column(name = "phone_number", length = 15, nullable = false, unique = true)
	    private String phoneNumber;

	    @Column(name = "password", length = 255, nullable = false)
	    private String password;

	    @Enumerated(EnumType.STRING)
	    @Column(name = "role", nullable = false)
	    private Role role;

	    @Column(name = "is_active", nullable = false)
	    private boolean isActive = true;

	    @Column(name = "extension", columnDefinition = "json")
	    private String extension; // If you want to handle JSON as text

	    // Optional: Constructor for convenience
	    public UserEntity(String firstName, String lastName, String email, String phoneNumber, String password, Role role) {
	        this.firstName = firstName;
	        this.lastName = lastName;
	        this.email = email;
	        this.phoneNumber = phoneNumber;
	        this.password = password;
	        this.role = role;
	    }
}
