package com.urbanproperty.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.urbanproperty.dto.PropertyTypeDto;
import com.urbanproperty.service.PropertyTypeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class PropertyTypeController {

    private final PropertyTypeService propertyTypeService;

    @PostMapping("/admin/property-types")
    public ResponseEntity<?> createPropertyType(@Valid @RequestBody PropertyTypeDto dto) {
        PropertyTypeDto createdPropertyType = propertyTypeService.createPropertyType(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPropertyType);
    }

    @GetMapping("/property-types")
    public ResponseEntity<?> getAllPropertyTypes() {
        List<PropertyTypeDto> propertyTypes = propertyTypeService.getAllPropertyTypes();
        return ResponseEntity.status(HttpStatus.OK).body(propertyTypes);
    }

    @GetMapping("/property-types/{id}")
    public ResponseEntity<?> getPropertyTypeById(@PathVariable Long id) {
        PropertyTypeDto propertyType = propertyTypeService.getPropertyTypeById(id);
        return ResponseEntity.status(HttpStatus.OK).body(propertyType);
    }

    @PutMapping("/admin/property-types/{id}")
    public ResponseEntity<?> updatePropertyType(@PathVariable Long id, @Valid @RequestBody PropertyTypeDto dto) {
        PropertyTypeDto updatedPropertyType = propertyTypeService.updatePropertyType(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPropertyType);
    }

    @DeleteMapping("/admin/property-types/{id}")
    public ResponseEntity<?> deletePropertyType(@PathVariable Long id) {
        propertyTypeService.deletePropertyType(id);
        return ResponseEntity.status(HttpStatus.OK).body("PropertyType with id " + id + " has been successfully deleted.");
    }
}