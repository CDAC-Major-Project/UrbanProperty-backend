package com.urbanproperty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.urbanproperty.dto.UserRegistrationRequest;
import com.urbanproperty.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping
	public ResponseEntity<?> register(@RequestBody @Valid UserRegistrationRequest dto) {
		System.out.println("User Registration: " + dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(dto));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> get_user_by_id(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.getUserById(id));
	}
	
	@GetMapping
	public ResponseEntity<?> get_all_users(){
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.getAllUsers());
	}
}
