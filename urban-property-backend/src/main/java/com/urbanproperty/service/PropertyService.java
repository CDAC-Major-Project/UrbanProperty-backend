package com.urbanproperty.service;

import com.urbanproperty.dto.PropertyRequestDto;
import com.urbanproperty.dto.PropertyResponseDto;
import java.util.List;

public interface PropertyService {
    PropertyResponseDto createProperty(PropertyRequestDto request);
    PropertyResponseDto getPropertyById(Long id);
    List<PropertyResponseDto> getAllActiveProperties();
    PropertyResponseDto addImageToProperty(Long propertyId, String imageUrl);
    // Update and Delete methods can be added here later
}