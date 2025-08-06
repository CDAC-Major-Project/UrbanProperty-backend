package com.urbanproperty.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import com.urbanproperty.entities.PropertyStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyResponseDto {
    private Long id;
    private String title;
    private String description;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private BigDecimal startingPrice;
    private PropertyStatus status;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    // Nested DTOs for rich responses
    private UserResponse seller;
    private PropertyTypeDto propertyType;
    private Set<AmenityDto> amenities;
    private Set<String> images; // Just the URLs
}