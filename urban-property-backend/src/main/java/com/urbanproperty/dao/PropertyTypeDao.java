package com.urbanproperty.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.urbanproperty.entities.PropertyType;

@Repository
public interface PropertyTypeDao extends JpaRepository<PropertyType, Long> {
    
}