package com.urbanproperty.service;

import java.util.List;

import com.urbanproperty.dto.PropertyTypeDto;
import com.urbanproperty.dto.PropertyTypeWithCountDto;

public interface PropertyTypeService {
    
	List<PropertyTypeWithCountDto> createPropertyType(PropertyTypeDto dto);

    PropertyTypeDto getPropertyTypeById(Long id);

    List<PropertyTypeWithCountDto> getAllPropertyTypes();

    List<PropertyTypeWithCountDto> updatePropertyType(Long id, PropertyTypeDto dto);

    List<PropertyTypeWithCountDto> deletePropertyType(Long id);
}