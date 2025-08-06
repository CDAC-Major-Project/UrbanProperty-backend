package com.urbanproperty.dto;

import com.urbanproperty.entities.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
	 private String firstName;
	    private String lastName;
	    private String email;
	    private String phoneNumber;
	    private Role role;
	    private boolean isActive;
}
