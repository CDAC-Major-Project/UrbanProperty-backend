package com.urbanproperty.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.urbanproperty.custom_exceptions.ApiException;
import com.urbanproperty.custom_exceptions.ResourceNotFoundException;
import com.urbanproperty.dao.AmenityDao;
import com.urbanproperty.dto.AmenityDto;
import com.urbanproperty.entities.Amenity;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class AmenityServiceImpl implements AmenityService {

    private final AmenityDao amenityDao;
    private final ModelMapper modelMapper; 

    @Override
    public AmenityDto createAmenity(AmenityDto dto) {
    	if (amenityDao.existsByName(dto.getName())) {
            throw new ApiException("Amenity with name '" + dto.getName() + "' already exists.");
        }
        // Use ModelMapper to convert DTO to entity
        Amenity amenity = modelMapper.map(dto, Amenity.class);
        Amenity savedAmenity = amenityDao.save(amenity);
        // Use ModelMapper to convert saved entity back to DTO
        return modelMapper.map(savedAmenity, AmenityDto.class);
    }

    @Override
    public AmenityDto getAmenityById(Long id) {
        Amenity amenity = amenityDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id: " + id));
        // Use ModelMapper to convert entity to DTO
        return modelMapper.map(amenity, AmenityDto.class);
    }

    @Override
    public List<AmenityDto> getAllAmenities() {
        return amenityDao.findAll().stream()
                // Use ModelMapper within the stream to convert each entity
                .map(amenity -> modelMapper.map(amenity, AmenityDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public AmenityDto updateAmenity(Long id, AmenityDto dto) {
        Amenity existingAmenity = amenityDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id: " + id));

        // Check if the new name is already taken by ANOTHER amenity
        amenityDao.findByName(dto.getName()).ifPresent(foundAmenity -> {
            if (!foundAmenity.getId().equals(existingAmenity.getId())) {
                // If the IDs are different, it's a duplicate violation
                throw new ApiException("Amenity with name '" + dto.getName() + "' already exists.");
            }
        });
        // Use ModelMapper to update the existing entity's fields from the DTO
        modelMapper.map(dto, existingAmenity);

        Amenity updatedAmenity = amenityDao.save(existingAmenity);
        return modelMapper.map(updatedAmenity, AmenityDto.class);
    }

    @Override
    public void deleteAmenity(Long id) {
        if (!amenityDao.existsById(id)) {
            throw new ResourceNotFoundException("Amenity not found with id: " + id);
        }
        amenityDao.deleteById(id);
    }

}