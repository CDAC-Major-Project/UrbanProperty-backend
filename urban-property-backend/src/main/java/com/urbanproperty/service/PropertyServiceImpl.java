package com.urbanproperty.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.urbanproperty.custom_exceptions.ResourceNotFoundException;
import com.urbanproperty.dao.AmenityDao;
import com.urbanproperty.dao.PropertyDao;
import com.urbanproperty.dao.PropertyTypeDao;
import com.urbanproperty.dao.UserDao;
import com.urbanproperty.dto.AmenityDto;
import com.urbanproperty.dto.PropertyRequestDto;
import com.urbanproperty.dto.PropertyResponseDto;
import com.urbanproperty.dto.PropertyTypeDto;
import com.urbanproperty.dto.UserResponse;
import com.urbanproperty.entities.Property;
import com.urbanproperty.entities.PropertyImage;
import com.urbanproperty.entities.PropertyStatus;
import com.urbanproperty.entities.PropertyType;
import com.urbanproperty.entities.UserEntity;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyDao propertyDao;
    private final UserDao userDao;
    private final PropertyTypeDao propertyTypeDao;
    private final AmenityDao amenityDao;
    private final ModelMapper mapper;

    @Override
    public PropertyResponseDto createProperty(PropertyRequestDto request) {
        UserEntity seller = userDao.findById(request.getSellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + request.getSellerId()));

        PropertyType propertyType = propertyTypeDao.findById(request.getPropertyTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("PropertyType not found with id: " + request.getPropertyTypeId()));

        List<Amenity> amenitiesList = amenityDao.findAllById(request.getAmenityIds());
        if (amenitiesList.size() != request.getAmenityIds().size()) {
            throw new ResourceNotFoundException("One or more amenities not found.");
        }
        Set<Amenity> amenities = new HashSet<>(amenitiesList);

        Property property = mapper.map(request, Property.class);
        property.setSeller(seller);
        property.setPropertyType(propertyType);
        property.setAmenities(amenities);
        property.setStatus(PropertyStatus.PENDING_APPROVAL);

        Property savedProperty = propertyDao.save(property);
        return mapToResponseDto(savedProperty);
    }

    @Override
    public PropertyResponseDto getPropertyById(Long id) {
        Property property = propertyDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));
        return mapToResponseDto(property);
    }

    @Override
    public List<PropertyResponseDto> getAllActiveProperties() {
        List<Property> activeProperties = propertyDao.findByStatus(PropertyStatus.ACTIVE);
        return activeProperties.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public PropertyResponseDto addImageToProperty(Long propertyId, String imageUrl) {
        Property property = propertyDao.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));

        PropertyImage newImage = new PropertyImage();
        newImage.setImageUrl(imageUrl);
        
        // Make first image primary by default
        if (property.getImages().isEmpty()) {
            newImage.setPrimary(true);
        }
        
        property.addImage(newImage);
        
        Property updatedProperty = propertyDao.save(property);
        return mapToResponseDto(updatedProperty);
    }
    
    // Helper to map Entity to DTO
    private PropertyResponseDto mapToResponseDto(Property property) {
        PropertyResponseDto dto = mapper.map(property, PropertyResponseDto.class);
        // Manually map nested DTOs if ModelMapper has issues with deep mapping
        dto.setSeller(mapper.map(property.getSeller(), UserResponse.class));
        dto.setPropertyType(mapper.map(property.getPropertyType(), PropertyTypeDto.class));
        dto.setAmenities(property.getAmenities().stream()
                .map(amenity -> mapper.map(amenity, AmenityDto.class))
                .collect(Collectors.toSet()));
        dto.setImages(property.getImages().stream()
                .map(PropertyImage::getImageUrl)
                .collect(Collectors.toSet()));
        return dto;
    }
}