package com.urbanproperty.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.urbanproperty.entities.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
		@JsonProperty(access =Access.READ_ONLY)
		private Long id;
	 	private String firstName;
	    private String lastName;
	    private String email;
	    private String phoneNumber;
	    private Role role;
	    private boolean isActive;
}
