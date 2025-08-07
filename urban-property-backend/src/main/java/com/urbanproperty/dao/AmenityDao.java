package com.urbanproperty.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.urbanproperty.entities.Amenity;

@Repository
public interface AmenityDao extends JpaRepository<Amenity, Long> {
	Optional<Amenity> findByName(String name);
	boolean existsByName(String name);
}