package com.urbanproperty.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.urbanproperty.custom_exceptions.ResourceNotFoundException;
import com.urbanproperty.dao.PropertyTypeDao;
import com.urbanproperty.dto.PropertyTypeDto;
import com.urbanproperty.entities.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@AllArgsConstructor
@Service
@Transactional 
public class PropertyTypeServiceImpl implements PropertyTypeService {

	
    private final PropertyTypeDao propertyTypeDao;

    @Override
    public PropertyTypeDto createPropertyType(PropertyTypeDto dto) {
        PropertyType propertyType = mapToEntity(dto);
        PropertyType savedEntity = propertyTypeDao.save(propertyType);
        return mapToDto(savedEntity);
    }

    @Override
    public PropertyTypeDto getPropertyTypeById(Long id) {
        PropertyType propertyType = propertyTypeDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PropertyType not found with id: " + id));
        return mapToDto(propertyType);
    }

    @Override
    public List<PropertyTypeDto> getAllPropertyTypes() {
        return propertyTypeDao.findAll().stream()
                .map(entity -> this.mapToDto(entity))
                .collect(Collectors.toList());
    }
    
    @Override
    public PropertyTypeDto updatePropertyType(Long id, PropertyTypeDto dto) {
        PropertyType existingEntity = propertyTypeDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PropertyType not found with id: " + id));

        existingEntity.setName(dto.getName());
        existingEntity.setDescription(dto.getDescription());

        PropertyType updatedEntity = propertyTypeDao.save(existingEntity);
        return mapToDto(updatedEntity);
    }

    @Override
    public void deletePropertyType(Long id) {
        if (!propertyTypeDao.existsById(id)) {
            throw new ResourceNotFoundException("PropertyType not found with id: " + id);
        }
        propertyTypeDao.deleteById(id);
    }

    private PropertyType mapToEntity(PropertyTypeDto dto) {
        PropertyType entity = new PropertyType();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    private PropertyTypeDto mapToDto(PropertyType entity) {
        return new PropertyTypeDto(
            entity.getId(),
            entity.getName(),
            entity.getDescription()
        );
    }
	
}