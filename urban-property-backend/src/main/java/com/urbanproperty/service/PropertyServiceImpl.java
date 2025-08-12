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
import com.urbanproperty.dto.PropertyDetailsDto;
import com.urbanproperty.dto.PropertyRequestDto;
import com.urbanproperty.dto.PropertyResponseDto;
import com.urbanproperty.entities.Amenity;
import com.urbanproperty.entities.Property;
import com.urbanproperty.entities.PropertyDetails;
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

        if (request.getDetails() != null) {
            PropertyDetails details = mapper.map(request.getDetails(), PropertyDetails.class);
            property.setDetails(details); // Use the helper method to set the bidirectional link
        }
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
    	// 1. Let ModelMapper handle all direct and nested mappings first.
        PropertyResponseDto dto = mapper.map(property, PropertyResponseDto.class);

        // 2. Manually handle the custom mapping for the images.
        if (property.getImages() != null) {
            Set<String> imageUrls = property.getImages().stream()
                                            .map(PropertyImage::getImageUrl)
                                            .collect(Collectors.toSet());
            dto.setImages(imageUrls);
        }
        if (property.getDetails() != null) {
            dto.setDetails(mapper.map(property.getDetails(), PropertyDetailsDto.class));
        }
        return dto;
    }
}