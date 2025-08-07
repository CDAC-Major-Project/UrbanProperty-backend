package com.urbanproperty.service;

import java.util.List;

import com.urbanproperty.dto.AllUsersResponse;
import com.urbanproperty.dto.LoginRequestDto;
import com.urbanproperty.dto.LoginResponseDto;
import com.urbanproperty.dto.PropertyResponseDto;
import com.urbanproperty.dto.UserRegistrationRequest;
import com.urbanproperty.dto.UserResponse;
import com.urbanproperty.dto.admin.AdminDashboardStatsDTO;

public interface UserService {
	UserResponse registerUser(UserRegistrationRequest dto);

	UserResponse getUserById(Long user_id);

	/**
     * Retrieves all BUYER and SELLER users, separated by role.
     */
    AllUsersResponse getAllUsers();
    AdminDashboardStatsDTO getDashboardStatistics();
    
    LoginResponseDto loginUser(LoginRequestDto request);
    
    //for properties
    void addFavoriteProperty(Long userId, Long propertyId);
    void removeFavoriteProperty(Long userId, Long propertyId);
    List<PropertyResponseDto> getFavoriteProperties(Long userId);
}
