package com.urbanproperty.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.urbanproperty.dto.ApiResponse;
import com.urbanproperty.dto.LoginRequestDto;
import com.urbanproperty.dto.LoginResponseDto;
import com.urbanproperty.dto.PropertyResponseDto;
import com.urbanproperty.dto.UserRegistrationRequest;
import com.urbanproperty.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping
	@Operation(
		    summary = "Register a new User",
		    description = "Creates a new user with the role of either BUYER or SELLER. Registration as an ADMIN is not permitted through this public endpoint."
		)
	public ResponseEntity<?> register(@RequestBody @Valid UserRegistrationRequest dto) {
		System.out.println("User Registration: " + dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(dto));
	}
	
	 @Operation(summary = "User Login")
	    @PostMapping("/login")
	    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto request) {
	        LoginResponseDto response = userService.loginUser(request);
	        return ResponseEntity.ok(response);
	    }
	@GetMapping("/{id}")
	@Operation(
		    summary = "Get User by ID",
		    description = "Retrieves the public details of a single user based on their unique ID."
		)
	public ResponseEntity<?> get_user_by_id(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.getUserById(id));
	}
	
	@GetMapping
	@Operation(
		    summary = "Get All Users (Admin Only)",
		    description = "Retrieves a list of all users, grouped by role. This endpoint is protected and requires ADMIN privileges."
		)
	public ResponseEntity<?> get_all_users(){
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.getAllUsers());
	}
	
	@Operation(summary = "Add a property to user's favorites")
	@PostMapping("/{userId}/favorites/{propertyId}")
	public ResponseEntity<ApiResponse> addFavorite(@PathVariable Long userId, @PathVariable Long propertyId) {
	    userService.addFavoriteProperty(userId, propertyId);
	    return ResponseEntity.ok(new ApiResponse("Property added to favorites successfully.", true));
	}

	@Operation(summary = "Remove a property from user's favorites")
	@DeleteMapping("/{userId}/favorites/{propertyId}")
	public ResponseEntity<ApiResponse> removeFavorite(@PathVariable Long userId, @PathVariable Long propertyId) {
	    userService.removeFavoriteProperty(userId, propertyId);
	    return ResponseEntity.ok(new ApiResponse("Property removed from favorites successfully.", true));
	}

	@Operation(summary = "Get all favorite properties for a user")
	@GetMapping("/{userId}/favorites")
	public ResponseEntity<List<PropertyResponseDto>> getFavorites(@PathVariable Long userId) {
	    List<PropertyResponseDto> favorites = userService.getFavoriteProperties(userId);
	    return ResponseEntity.ok(favorites);
	}

}
