package com.urbanproperty.service;

import java.util.List;
import com.urbanproperty.dto.AmenityDto;

public interface AmenityService {
    
    AmenityDto createAmenity(AmenityDto dto);

    AmenityDto getAmenityById(Long id);

    List<AmenityDto> getAllAmenities();

    AmenityDto updateAmenity(Long id, AmenityDto dto);

    void deleteAmenity(Long id);
}