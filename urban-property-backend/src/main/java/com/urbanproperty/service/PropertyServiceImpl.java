package com.urbanproperty.service;

import java.io.IOException;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.urbanproperty.custom_exceptions.ApiException;
import com.urbanproperty.custom_exceptions.ResourceNotFoundException;
import com.urbanproperty.dao.AmenityDao;
import com.urbanproperty.dao.PropertyDao;
import com.urbanproperty.dao.PropertyTypeDao;
import com.urbanproperty.dao.UserDao;
import com.urbanproperty.dto.PropertyDetailsDto;
import com.urbanproperty.dto.PropertyRequestDto;
import com.urbanproperty.dto.PropertyResponseDto;
import com.urbanproperty.dto.PropertyUpdateDto;
import com.urbanproperty.dto.admin.MonthlyPropertyStatsDto;
import com.urbanproperty.dto.admin.PropertyStatusCountDto;
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
    private ImageUploadService imageUploadService;

    @Override
    public PropertyResponseDto createPropertyWithImage(PropertyRequestDto request, MultipartFile imageFile, Authentication authentication) throws IOException {
        
    	// Get the email of the logged-in user from the authenticat	ion token
        String sellerEmail = authentication.getName();
        
        // Finding seller in the database using their email
        UserEntity seller = userDao.findByEmail(sellerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user '" + sellerEmail + "' not found in database."));

        PropertyType propertyType = propertyTypeDao.findById(request.getPropertyTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("PropertyType not found with id: " + request.getPropertyTypeId()));

        Set<Amenity> amenities = new HashSet<>(amenityDao.findAllById(request.getAmenityIds()));

        Property property = mapper.map(request, Property.class);
        property.setSeller(seller);
        property.setPropertyType(propertyType);
        property.setAmenities(amenities);
        property.setStatus(PropertyStatus.PENDING); 
        
        if (request.getDetails() != null) {
            PropertyDetails details = mapper.map(request.getDetails(), PropertyDetails.class);
            property.setDetails(details);
        }
        
        // Save the property FIRST to generate its ID
        Property savedProperty = propertyDao.save(property);
        Long propertyId = savedProperty.getId();
        
        String originalFilename = imageFile.getOriginalFilename();
        String sanitizedFilename = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String publicId = "properties/" + propertyId + "/" + sanitizedFilename;
        
        Map uploadResult = imageUploadService.uploadImage(imageFile, publicId);

        PropertyImage newImage = new PropertyImage();
        newImage.setImageUrl((String) uploadResult.get("secure_url"));
        newImage.setPrimary(true);
        
        // Link the image to the property and save again to establish the relationship
        savedProperty.addImage(newImage);
        Property finalProperty = propertyDao.save(savedProperty);

        return mapToResponseDto(finalProperty);
    }

    @Override
    public PropertyResponseDto updateProperty(Long propertyId, PropertyUpdateDto updateDto, MultipartFile imageFile, Authentication authentication) throws IOException {
        
        // 1. Fetch the property and the currently logged-in user
        Property property = propertyDao.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));
        
        UserEntity seller = userDao.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        // 2. SECURITY CHECK: Verify that the logged-in user owns this property
        if (!property.getSeller().getId().equals(seller.getId())) {
            throw new ApiException("You do not have permission to edit this property.");
        }

        // 3. Handle Image Update (if a new image is provided)
        if (imageFile != null && !imageFile.isEmpty()) {
            if (property.getImages() != null && !property.getImages().isEmpty()) {
                PropertyImage oldImage = property.getImages().iterator().next();
                String folder = "properties/" + propertyId;
                // Upload new image
                String originalFilename = imageFile.getOriginalFilename();
                String sanitizedFilename = originalFilename.substring(0, originalFilename.lastIndexOf("."));
                String newPublicId = folder + "/" + sanitizedFilename;
                Map uploadResult = imageUploadService.uploadImage(imageFile, newPublicId);
                
                // Update the URL on the existing image entity
                oldImage.setImageUrl((String) uploadResult.get("secure_url"));
            }
        }

        // 4. Handle Text Data Update (if new data is provided)
        if (updateDto != null) {
            mapper.map(updateDto, property);
            
            // Handle relationships if they are part of the update
            if (updateDto.getPropertyTypeId() != null) {
                PropertyType propertyType = propertyTypeDao.findById(updateDto.getPropertyTypeId()).orElseThrow(() -> new ResourceNotFoundException("PropertyType not found"));
                property.setPropertyType(propertyType);
            }
            if (updateDto.getAmenityIds() != null && !updateDto.getAmenityIds().isEmpty()) {
                Set<Amenity> amenities = new HashSet<>(amenityDao.findAllById(updateDto.getAmenityIds()));
                property.setAmenities(amenities);
            }
        }

        // 5. Save the updated property and return the response
        Property updatedProperty = propertyDao.save(property);
        return mapToResponseDto(updatedProperty);
    }
    
    @Override
    public PropertyResponseDto getPropertyById(Long id) {
        Property property = propertyDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));
        return mapToResponseDto(property);
    }
    
    @Override
    public List<PropertyResponseDto> getAllPropertiesBySeller(Long sellerId) {
        // 1. Call the new DAO method to fetch all properties for the given seller.
        List<Property> properties = propertyDao.findBySellerId(sellerId);
        
        // 2. Stream the list of entities and map each one to a response DTO.
        return properties.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PropertyResponseDto> getAllActiveProperties() {
        List<Property> activeProperties = propertyDao.findByStatus(PropertyStatus.ACTIVE);
        return activeProperties.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public PropertyResponseDto addImageToProperty(Long propertyId, MultipartFile imageFile) throws IOException {
        Property property = propertyDao.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));

        String originalFilename = imageFile.getOriginalFilename();
        String sanitizedFilename = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String publicId = "properties"+"/"+ propertyId + "/" + sanitizedFilename;
        
        Map uploadResult;
        try {
            uploadResult = imageUploadService.uploadImage(imageFile, publicId);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("resource already exists")) {
                throw new ApiException("An image with the name '" + originalFilename + "' already exists for this property.");
            }
            throw e;
        }
        
        PropertyImage newImage = new PropertyImage();
        newImage.setImageUrl((String) uploadResult.get("secure_url"));
        
        // Make first image primary by default
        if (property.getImages() == null || property.getImages().isEmpty()) {
            newImage.setPrimary(true);
        }
        
        property.addImage(newImage);
        Property updatedProperty = propertyDao.save(property);
        return mapper.map(updatedProperty, PropertyResponseDto.class);
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
    
    //for admin dashboard page, getting the status count
    @Override
    public Map<PropertyStatus, Long> getPropertyStatusCounts() {
        List<PropertyStatusCountDto> counts = propertyDao.countPropertiesByStatus();

        // 2. Converting the list into Map using Java Streams
        return counts.stream()
                .collect(Collectors.toMap(
                    PropertyStatusCountDto::getStatus,
                    PropertyStatusCountDto::getCount
                ));
    }
    
    @Override
    public Map<String, Long> getMonthlyPropertyStatsForCurrentYear() {
        // Use a LinkedHashMap to maintain the order in which months are inserted.
        Map<String, Long> monthlyStatsMap = new LinkedHashMap<>();
        
        for (int i = 1; i <= 12; i++) {
            String monthName = Month.of(i).getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
            monthlyStatsMap.put(monthName, 0L);
        }

        // Fetch and update with actual counts
        List<MonthlyPropertyStatsDto> dbStats = propertyDao.countPropertiesByMonthForCurrentYear();
        dbStats.forEach(stat -> {
            String monthName = Month.of(stat.getMonth()).getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
            monthlyStatsMap.put(monthName, stat.getCount());
        });

        return monthlyStatsMap;
    }
}