package com.urbanproperty.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.urbanproperty.dto.PropertyRequestDto;
import com.urbanproperty.dto.PropertyResponseDto;
import com.urbanproperty.service.PropertyService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/properties")
@AllArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @Operation(summary = "Create a new Property Listing (Seller Only)")
    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<PropertyResponseDto> createProperty(@Valid @RequestBody PropertyRequestDto request) {
        PropertyResponseDto createdProperty = propertyService.createProperty(request);
        return new ResponseEntity<>(createdProperty, HttpStatus.CREATED);
    }

    @Operation(summary = "Get All Active Properties (Public)")
    @GetMapping
    public ResponseEntity<List<PropertyResponseDto>> getAllActiveProperties() {
        List<PropertyResponseDto> properties = propertyService.getAllActiveProperties();
        return ResponseEntity.ok(properties);
    }

    @Operation(summary = "Get a Single Property by ID (Public)")
    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponseDto> getPropertyById(@PathVariable Long id) {
        PropertyResponseDto property = propertyService.getPropertyById(id);
        return ResponseEntity.ok(property);
    }
    @Operation(summary = "Get All Properties for a Specific Seller (Seller or Admin Only)")
    @GetMapping("/seller/{sellerId}")
    @PreAuthorize("@customSecurity.isOwnerOrAdmin(authentication, #sellerId)")
    public ResponseEntity<List<PropertyResponseDto>> getAllPropertiesBySeller(@PathVariable Long sellerId) {
        List<PropertyResponseDto> properties = propertyService.getAllPropertiesBySeller(sellerId);
        return ResponseEntity.ok(properties);
    }

    @Operation(summary = "Add an Image to a Property")
    @PostMapping("/{id}/images")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<PropertyResponseDto> addImageToProperty(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String imageUrl = payload.get("imageUrl");
        if (imageUrl == null || imageUrl.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        PropertyResponseDto updatedProperty = propertyService.addImageToProperty(id, imageUrl);
        return ResponseEntity.ok(updatedProperty);
    }
}