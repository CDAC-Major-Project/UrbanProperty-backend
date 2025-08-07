package com.urbanproperty.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.urbanproperty.dto.AmenityDto;
import com.urbanproperty.dto.ApiResponse;
import com.urbanproperty.service.AmenityService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/amenities")
@AllArgsConstructor
public class AmenityController {

    private final AmenityService amenityService;

    @PostMapping
    @Operation(
    	    summary = "Create a new Amenity (Admin Only)",
    	    description = "Adds a new amenity to the system that can be associated with properties (e.g., 'Swimming Pool', 'Gym'). Requires ADMIN privileges."
    	)
    public ResponseEntity<?> createAmenity(@Valid @RequestBody AmenityDto dto) {
        AmenityDto createdAmenity = amenityService.createAmenity(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAmenity);
    }

    @GetMapping
    @Operation(
    	    summary = "Get All Amenities",
    	    description = "Retrieves a list of all available amenities. This is a public endpoint."
    	)
    public ResponseEntity<?> getAllAmenities() {
        List<AmenityDto> amenities = amenityService.getAllAmenities();
        return ResponseEntity.status(HttpStatus.OK).body(amenities);
    }
    
    @GetMapping("/{id}")
    @Operation(
    	    summary = "Get Amenity by ID",
    	    description = "Retrieves a single amenity by its unique ID. This is a public endpoint."
    	)
    public ResponseEntity<?> getAmenityById(@PathVariable("id") Long id) {
        AmenityDto amenityDto = amenityService.getAmenityById(id);
        return ResponseEntity.status(HttpStatus.OK).body(amenityDto);
    }
    
    @PutMapping("/{id}")
    @Operation(
    	    summary = "Update an Amenity (Admin Only)",
    	    description = "Updates the name and/or icon URL of an existing amenity. Requires ADMIN privileges."
    	)
    public ResponseEntity<?> updateAmenity(@PathVariable("id") Long id, @Valid @RequestBody AmenityDto dto) {
        AmenityDto updatedAmenity = amenityService.updateAmenity(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedAmenity);
    }

    @DeleteMapping("/{id}")
    @Operation(
    	    summary = "Delete an Amenity (Admin Only)",
    	    description = "Deletes an amenity from the system based on its unique ID. Requires ADMIN privileges."
    	)
    public ResponseEntity<?> deleteAmenity(@PathVariable("id") Long id) {
        amenityService.deleteAmenity(id);
        return ResponseEntity.ok(new ApiResponse("Amenity with id " + id + " has been successfully deleted.", true));
    }
}