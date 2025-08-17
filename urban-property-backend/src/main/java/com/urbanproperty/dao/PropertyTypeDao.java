package com.urbanproperty.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.urbanproperty.dto.PropertyTypeWithCountDto;
import com.urbanproperty.entities.PropertyType;

@Repository
public interface PropertyTypeDao extends JpaRepository<PropertyType, Long> {
	boolean existsByName(String name);
	Optional<PropertyType> findByName(String name); 
	
	@Query("SELECT new com.urbanproperty.dto.PropertyTypeWithCountDto(pt.id, pt.name, pt.description, pt.createdTime, size(pt.properties)) FROM PropertyType pt")
    List<PropertyTypeWithCountDto> findAllWithPropertyCount();
}