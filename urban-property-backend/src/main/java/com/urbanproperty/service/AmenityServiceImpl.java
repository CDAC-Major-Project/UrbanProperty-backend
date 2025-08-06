package com.urbanproperty.service;

import com.urbanproperty.custom_exceptions.ResourceNotFoundException;
import com.urbanproperty.dao.AmenityDao;
import com.urbanproperty.dto.AmenityDto;
import com.urbanproperty.entities.Amenity;
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

    @Override
    public AmenityDto createAmenity(AmenityDto dto) {
        Amenity amenity = mapToEntity(dto);
        Amenity savedAmenity = amenityDao.save(amenity);
        return mapToDto(savedAmenity);
    }

    @Override
   
    public AmenityDto getAmenityById(Long id) {
        Amenity amenity = amenityDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id: " + id));
        return mapToDto(amenity);
    }

    @Override
    public List<AmenityDto> getAllAmenity() {
        return amenityDao.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public AmenityDto updateAmenity(Long id, AmenityDto dto) {
        Amenity existingAmenity = amenityDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id: " + id));

        existingAmenity.setName(dto.getName());
        existingAmenity.setIconUrl(dto.getIconUrl());

        Amenity updatedAmenity = amenityDao.save(existingAmenity);
        return mapToDto(updatedAmenity);
    }

    @Override
    public void deleteAmenity(Long id) {
        if (!amenityDao.existsById(id)) {
            throw new ResourceNotFoundException("Amenity not found with id: " + id);
        }
        amenityDao.deleteById(id);
    }

    private Amenity mapToEntity(AmenityDto dto) {
        Amenity amenity = new Amenity();
        amenity.setName(dto.getName());
        amenity.setIconUrl(dto.getIconUrl());
        return amenity;
    }

    private AmenityDto mapToDto(Amenity amenity) {
        return new AmenityDto(
            amenity.getId(),
            amenity.getName(),
            amenity.getIconUrl()
        );
    }
}