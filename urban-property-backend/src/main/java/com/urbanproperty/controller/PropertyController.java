package com.urbanproperty.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urbanproperty.custom_exceptions.ApiException;
import com.urbanproperty.dto.PropertyRequestDto;
import com.urbanproperty.dto.PropertyResponseDto;
import com.urbanproperty.dto.PropertyUpdateDto;
import com.urbanproperty.service.PropertyService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/properties")
@AllArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;
    private final ObjectMapper objectMapper; // Injecting ObjectMapper

    @Operation(summary = "Create a new Property Listing with an Image (Seller Only)")
    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<PropertyResponseDto> createProperty(
            @RequestParam("propertyData") String propertyDataString,
            @RequestParam("image") MultipartFile imageFile,
            Authentication authentication
    ) throws IOException {
    	// deserializing the JSON string back into DTO
        PropertyRequestDto requestDto = objectMapper.readValue(propertyDataString, PropertyRequestDto.class);
        
        PropertyResponseDto createdProperty = propertyService.createPropertyWithImage(requestDto, imageFile, authentication);
        return new ResponseEntity<>(createdProperty, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an Existing Property (seller Only)")
    @PutMapping("/{propertyId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<PropertyResponseDto> updateProperty(
            @PathVariable Long propertyId,
            @RequestParam(name = "propertyData", required = false) String updatePropertyString,
            @RequestParam(name = "image", required = false) MultipartFile imageFile,
            Authentication authentication
    ) throws IOException {

        if (updatePropertyString == null && (imageFile == null || imageFile.isEmpty())) {
            throw new ApiException("No update data provided. Please provide either property data or a new image.");
        }

        PropertyUpdateDto updateDto = null;
        if (updatePropertyString != null) {
            // Deserialize the JSON string to our new DTO
            updateDto = objectMapper.readValue(updatePropertyString, PropertyUpdateDto.class);
        }
        
        PropertyResponseDto updatedProperty = propertyService.updateProperty(propertyId, updateDto, imageFile, authentication);
        return ResponseEntity.ok(updatedProperty);
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
    @PostMapping("/{propertyId}/images")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<PropertyResponseDto> uploadImageForProperty(
            @PathVariable Long propertyId,
            @RequestParam("image") MultipartFile file) throws IOException {
    	 if (file.isEmpty()) {
             return ResponseEntity.badRequest().build();
         }

         PropertyResponseDto updatedProperty = propertyService.addImageToProperty(propertyId, file);
        return ResponseEntity.ok(updatedProperty);
    }
}