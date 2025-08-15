package com.urbanproperty.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyUpdateDto {
    private String title;
    private String description;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private BigDecimal startingPrice;
    private Long propertyTypeId;
    private List<Long> amenityIds;
    private PropertyDetailsDto details;
}