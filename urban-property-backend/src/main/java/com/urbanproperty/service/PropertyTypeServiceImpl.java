package com.urbanproperty.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.urbanproperty.custom_exceptions.ApiException;
import com.urbanproperty.custom_exceptions.ResourceNotFoundException;
import com.urbanproperty.dao.PropertyTypeDao;
import com.urbanproperty.dto.PropertyTypeDto;
import com.urbanproperty.entities.PropertyType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@Service
@Transactional 
public class PropertyTypeServiceImpl implements PropertyTypeService {

	
    private final PropertyTypeDao propertyTypeDao;

    @Override
    public PropertyTypeDto createPropertyType(PropertyTypeDto dto) {
    	// 1. validating property type
        if (propertyTypeDao.existsByName(dto.getName())) {
            throw new ApiException("Property Type with name '" + dto.getName() + "' already exists.");
        }
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

        // Check if the new name is already taken by another property type
        propertyTypeDao.findByName(dto.getName()).ifPresent(foundEntity -> {
            // If an entity is found with the new name, check if its ID is different from the current entity's ID
            if (!foundEntity.getId().equals(existingEntity.getId())) {
                // If the IDs are different, it's a duplicate name violation
                throw new ApiException("Property Type with name '" + dto.getName() + "' already exists.");
            }
        });
        // If validation passes, update the entity's fields
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