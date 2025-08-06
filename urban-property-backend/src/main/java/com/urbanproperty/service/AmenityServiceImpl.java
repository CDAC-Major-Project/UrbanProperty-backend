package com.urbanproperty.service;

import com.urbanproperty.custom_exceptions.ResourceNotFoundException;
import com.urbanproperty.dao.AmenityDao;
import com.urbanproperty.dto.AmenityDto;
import com.urbanproperty.entities.Amenity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class AmenityServiceImpl implements AmenityService {

    private final AmenityDao amenityDao;
    private final ModelMapper modelMapper; 

    @Override
    public AmenityDto createAmenity(AmenityDto dto) {
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
    public List<AmenityDto> getAllAmenity() {
        return amenityDao.findAll().stream()
                // Use ModelMapper within the stream to convert each entity
                .map(amenity -> modelMapper.map(amenity, AmenityDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public AmenityDto updateAmenity(Long id, AmenityDto dto) {
        Amenity existingAmenity = amenityDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id: " + id));

        // Use ModelMapper to update the existing entity's fields from the DTO
        // This is a powerful feature for updating objects without manually setting each
        // field.
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