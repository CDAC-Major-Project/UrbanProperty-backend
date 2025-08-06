package com.urbanproperty.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.urbanproperty.entities.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
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
	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number format")
	private String phoneNumber;

	@NotBlank(message = "Password is required")
	@Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[#@$*]).{5,20})", message = "Password must contain a digit, lowercase letter, special character [#@$*] and be 5-20 characters long")
	private String password;

	@NotNull(message = "Role is required")
	private Role role;

}
