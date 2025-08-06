package com.urbanproperty.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.urbanproperty.entities.*;

public interface UserDao extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsByPhoneNumber(String phoneNumber);
}
