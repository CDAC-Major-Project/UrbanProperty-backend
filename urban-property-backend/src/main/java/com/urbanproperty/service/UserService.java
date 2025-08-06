package com.urbanproperty.service;

import com.urbanproperty.dto.AllUsersResponse;
import com.urbanproperty.dto.UserRegistrationRequest;
import com.urbanproperty.dto.UserResponse;

public interface UserService {
	UserResponse registerUser(UserRegistrationRequest dto);

	UserResponse getUserById(Long user_id);

	/**
     * Retrieves all BUYER and SELLER users, separated by role.
     */
    AllUsersResponse getAllUsers();

}
