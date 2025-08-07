package com.urbanproperty.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.urbanproperty.custom_exceptions.ApiException;
import com.urbanproperty.custom_exceptions.ResourceNotFoundException;
import com.urbanproperty.dao.PropertyDao;
import com.urbanproperty.dao.UserDao;
import com.urbanproperty.dto.AllUsersResponse;
import com.urbanproperty.dto.LoginRequestDto;
import com.urbanproperty.dto.LoginResponseDto;
import com.urbanproperty.dto.PropertyResponseDto;
import com.urbanproperty.dto.UserRegistrationRequest;
import com.urbanproperty.dto.UserResponse;
import com.urbanproperty.entities.Property;
import com.urbanproperty.entities.Role;
import com.urbanproperty.entities.UserEntity;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PropertyDao propertyDao;
    private final ModelMapper mapper;

    @Override
    public UserResponse registerUser(UserRegistrationRequest dto) {
    	// Block ADMIN registration via the public API
        if (dto.getRole() == Role.ADMIN) {
            throw new ApiException("Admin registration is not allowed.");
        }
        
        if (userDao.existsByEmail(dto.getEmail()))
            throw new ApiException("Dup Email detected - User exists already!!!!");

        // Check if a user with the same phone number already exists
        if (userDao.existsByPhoneNumber(dto.getPhoneNumber())) {
            // UPDATED: More generic and secure error message
            throw new ApiException("Phone number is already registered.");
        }
        
        UserEntity entity = mapper.map(dto, UserEntity.class);
        UserEntity savedEntity = userDao.save(entity);
        return mapper.map(savedEntity, UserResponse.class);
    }
//
    @Override
    public LoginResponseDto loginUser(LoginRequestDto request) {
        // 1. Find the user by email
        UserEntity user = userDao.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        // 2. Check if the provided password matches the stored password
        if (!user.getPassword().equals(request.getPassword())) {
            throw new ApiException("Invalid email or password.");
        }

        // 3. If credentials are correct, map to response DTOs
        UserResponse userDetails = mapper.map(user, UserResponse.class);
        
        return new LoginResponseDto("Login successful!", userDetails);
    }

    @Override
    public UserResponse getUserById(Long userId) {
        UserEntity user = userDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return mapper.map(user, UserResponse.class);
    }
//
    @Override
    public AllUsersResponse getAllUsers() {
        List<UserEntity> allUsers = userDao.findAll();

        // Use Java Streams to group users by their role into a Map
        Map<Role, List<UserResponse>> usersByRole = allUsers.stream()
                .filter(user -> user.getRole() == Role.BUYER || user.getRole() == Role.SELLER) // Exclude ADMINs
                .collect(Collectors.groupingBy(
                		UserEntity::getRole,
                        Collectors.mapping(user -> mapper.map(user, UserResponse.class), Collectors.toList())
                ));

        // Create the response object
        AllUsersResponse response = new AllUsersResponse();
        response.setBuyer(usersByRole.getOrDefault(Role.BUYER, Collections.emptyList()));
        response.setSeller(usersByRole.getOrDefault(Role.SELLER, Collections.emptyList()));

        return response;
    }
    
    //for for adding and removing property as favourite
    @Override
    public void addFavoriteProperty(Long userId, Long propertyId) {
        UserEntity user = userDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Property property = propertyDao.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));
        
        user.addFavorite(property);
        // No need to call save, @Transactional will handle it.
    }

    @Override
    public void removeFavoriteProperty(Long userId, Long propertyId) {
        UserEntity user = userDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Property property = propertyDao.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));

        user.removeFavorite(property);
    }
    @Override
    public List<PropertyResponseDto> getFavoriteProperties(Long userId) {
        UserEntity user = userDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return user.getFavoriteProperties().stream()
                // Assuming you have a helper method or can create one to map Property to PropertyResponseDto
                .map(property -> mapper.map(property, PropertyResponseDto.class))
                .collect(Collectors.toList());
    }
}
