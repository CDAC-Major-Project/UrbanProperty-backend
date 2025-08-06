package com.urbanproperty.service;

import java.util.List;

import com.urbanproperty.dto.UserRegistrationRequest;
import com.urbanproperty.dto.UserResponse;

public interface UserService {
	UserResponse registerUser(UserRegistrationRequest dto);

	UserResponse getUserById(Long user_id);

	List<UserResponse> getAllUsers();

}
