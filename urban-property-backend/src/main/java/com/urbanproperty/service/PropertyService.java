package com.urbanproperty.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.urbanproperty.dto.PropertyRequestDto;
import com.urbanproperty.dto.PropertyResponseDto;

public interface PropertyService {
    PropertyResponseDto createProperty(PropertyRequestDto request);
    PropertyResponseDto getPropertyById(Long id);
    List<PropertyResponseDto> getAllActiveProperties();
    PropertyResponseDto addImageToProperty(Long propertyId, MultipartFile imageFile) throws IOException;
    
    List<PropertyResponseDto> getAllPropertiesBySeller(Long sellerId);
    
    PropertyResponseDto createPropertyWithImage(PropertyRequestDto request, MultipartFile imageFile) throws IOException;

}