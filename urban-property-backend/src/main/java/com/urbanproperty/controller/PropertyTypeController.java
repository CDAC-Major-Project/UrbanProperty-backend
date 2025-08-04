package com.urbanproperty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.urbanproperty.dto.PropertyTypeDto;
import com.urbanproperty.service.PropertyTypeService;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PropertyTypeController {

    private final PropertyTypeService propertyTypeService;

    @Autowired
    public PropertyTypeController(PropertyTypeService propertyTypeService) {
        this.propertyTypeService = propertyTypeService;
    }

    @PostMapping("/admin/property-types")
    @ResponseStatus(HttpStatus.CREATED)
    public PropertyTypeDto createPropertyType(@Valid @RequestBody PropertyTypeDto dto) {
        return propertyTypeService.createPropertyType(dto);
    }

    @GetMapping("/property-types")
    public List<PropertyTypeDto> getAllPropertyTypes() {
        return propertyTypeService.getAllPropertyTypes();
    }

    @GetMapping("/property-types/{id}")
    public PropertyTypeDto getPropertyTypeById(@PathVariable Long id) {
        return propertyTypeService.getPropertyTypeById(id);
    }

    @PutMapping("/admin/property-types/{id}")
    public PropertyTypeDto updatePropertyType(@PathVariable Long id, @Valid @RequestBody PropertyTypeDto dto) {
        return propertyTypeService.updatePropertyType(id, dto);
    }

    @DeleteMapping("/admin/property-types/{id}")
    public ResponseEntity<String> deletePropertyType(@PathVariable Long id) {
        propertyTypeService.deletePropertyType(id);
        return new ResponseEntity<>("PropertyType with id " + id + " has been successfully deleted.", HttpStatus.OK);
    }
}
