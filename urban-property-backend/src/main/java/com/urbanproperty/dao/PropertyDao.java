package com.urbanproperty.dao;

import com.urbanproperty.entities.Property;
import com.urbanproperty.entities.PropertyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyDao extends JpaRepository<Property, Long> {
    List<Property> findByStatus(PropertyStatus status);
    
    List<Property> findBySellerId(Long sellerId);
}