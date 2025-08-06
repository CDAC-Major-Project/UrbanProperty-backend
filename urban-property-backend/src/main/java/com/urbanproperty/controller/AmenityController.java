package com.urbanproperty.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.urbanproperty.dto.AmenityDto;
import com.urbanproperty.service.AmenityService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class AmenityController {

    private final AmenityService amenityService;

    @PostMapping("/seller/amenities")
    public ResponseEntity<?> createAmenity(@Valid @RequestBody AmenityDto dto) {
        AmenityDto createdAmenity = amenityService.createAmenity(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAmenity);
    }

    @GetMapping("/amenities")
    public ResponseEntity<?> getAllAmenities() {
        List<AmenityDto> amenities = amenityService.getAllAmenity();
        return ResponseEntity.status(HttpStatus.OK).body(amenities);
    }
    
    @GetMapping("/amenities/{id}")
    public ResponseEntity<?> getAmenityById(@PathVariable("id") Long id) {
        AmenityDto amenityDto = amenityService.getAmenityById(id);
        return ResponseEntity.status(HttpStatus.OK).body(amenityDto);
    }
    
    @PutMapping("/seller/amenities/{id}")
    public ResponseEntity<?> updateAmenity(@PathVariable("id") Long id, @Valid @RequestBody AmenityDto dto) {
        AmenityDto updatedAmenity = amenityService.updateAmenity(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedAmenity);
    }

    @DeleteMapping("/seller/amenities/{id}")
    public ResponseEntity<?> deleteAmenity(@PathVariable("id") Long id) {
        amenityService.deleteAmenity(id);
        return ResponseEntity.status(HttpStatus.OK).body("Amenity with id " + id + " has been successfully deleted.");
    }
}