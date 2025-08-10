package com.urbanproperty.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.urbanproperty.dto.PropertyTypeDto;
import com.urbanproperty.service.PropertyTypeService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/property-types")
@AllArgsConstructor
public class PropertyTypeController {

    private final PropertyTypeService propertyTypeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
    	    summary = "Create a new Property Type (Admin Only)",
    	    description = "Adds a new property type to the system (e.g., 'Residential', 'Commercial'). Requires ADMIN privileges."
    	)
    public ResponseEntity<?> createPropertyType(@Valid @RequestBody PropertyTypeDto dto) {
        PropertyTypeDto createdPropertyType = propertyTypeService.createPropertyType(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPropertyType);
    }

    @GetMapping
    @Operation(
    	    summary = "Get All Property Types",
    	    description = "Retrieves a list of all available property types. This is a public endpoint."
    	)
    public ResponseEntity<?> getAllPropertyTypes() {
        List<PropertyTypeDto> propertyTypes = propertyTypeService.getAllPropertyTypes();
        return ResponseEntity.status(HttpStatus.OK).body(propertyTypes);
    }

    @GetMapping("/{id}")
    @Operation(
    	    summary = "Get Property Type by ID",
    	    description = "Retrieves a single property type by its unique ID. This is a public endpoint."
    	)
    public ResponseEntity<?> getPropertyTypeById(@PathVariable Long id) {
        PropertyTypeDto propertyType = propertyTypeService.getPropertyTypeById(id);
        return ResponseEntity.status(HttpStatus.OK).body(propertyType);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
    	    summary = "Update a Property Type (Admin Only)",
    	    description = "Updates the name and/or description of an existing property type. Requires ADMIN privileges."
    	)
    public ResponseEntity<?> updatePropertyType(@PathVariable Long id, @Valid @RequestBody PropertyTypeDto dto) {
        PropertyTypeDto updatedPropertyType = propertyTypeService.updatePropertyType(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPropertyType);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
    	    summary = "Delete a Property Type (Admin Only)",
    	    description = "Deletes a property type from the system based on its unique ID. Requires ADMIN privileges."
    	)
    public ResponseEntity<?> deletePropertyType(@PathVariable Long id) {
        propertyTypeService.deletePropertyType(id);
        return ResponseEntity.status(HttpStatus.OK).body("PropertyType with id " + id + " has been successfully deleted.");
    }
}