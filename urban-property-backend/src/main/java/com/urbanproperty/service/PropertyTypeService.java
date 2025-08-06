package com.urbanproperty.service;

import java.util.List;
import com.urbanproperty.dto.PropertyTypeDto;

public interface PropertyTypeService {
    
    PropertyTypeDto createPropertyType(PropertyTypeDto dto);

    PropertyTypeDto getPropertyTypeById(Long id);

    List<PropertyTypeDto> getAllPropertyTypes();

    PropertyTypeDto updatePropertyType(Long id, PropertyTypeDto dto);

    void deletePropertyType(Long id);
}