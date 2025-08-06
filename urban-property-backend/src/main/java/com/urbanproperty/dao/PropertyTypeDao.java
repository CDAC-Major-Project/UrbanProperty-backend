package com.urbanproperty.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.urbanproperty.entities.PropertyType;

@Repository
public interface PropertyTypeDao extends JpaRepository<PropertyType, Long> {
	boolean existsByName(String name);
	Optional<PropertyType> findByName(String name); 
}