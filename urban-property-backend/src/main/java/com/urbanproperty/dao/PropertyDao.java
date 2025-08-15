package com.urbanproperty.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.urbanproperty.dto.PropertyStatusCountDto;
import com.urbanproperty.entities.Property;
import com.urbanproperty.entities.PropertyStatus;

@Repository
public interface PropertyDao extends JpaRepository<Property, Long> {
    List<Property> findByStatus(PropertyStatus status);
    
    List<Property> findBySellerId(Long sellerId);
    @Query("SELECT new com.urbanproperty.dto.PropertyStatusCountDto(p.status, COUNT(p)) FROM Property p GROUP BY p.status")
    List<PropertyStatusCountDto> countPropertiesByStatus();
}