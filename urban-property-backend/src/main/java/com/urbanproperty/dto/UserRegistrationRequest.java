package com.urbanproperty.dto;

import org.hibernate.validator.constraints.Length;

import com.urbanproperty.entities.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationRequest {

	@NotBlank(message = "First name is required")
	@Length(min = 3, max = 20, message = "First name must be between 3 to 20 characters")
	private String firstName;

	@NotBlank(message = "Last name is required")
	private String lastName;

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;

	@NotBlank(message = "Phone number is required")
	private String phoneNumber;

	@NotBlank(message = "Password is required")
    @Size(min = 5, max = 20, message = "Password must be between 5 and 20 characters long")
    @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one digit")
    @Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter")
    @Pattern(regexp = ".*[#@$*].*", message = "Password must contain at least one special character (#@$*)")
	private String password;

	@NotNull(message = "Role is required")
	private Role role;

}
