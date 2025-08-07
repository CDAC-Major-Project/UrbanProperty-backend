package com.urbanproperty.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.urbanproperty.dto.admin.UserMonthlyStats;
import com.urbanproperty.entities.UserEntity;
public interface UserDao extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsByPhoneNumber(String phoneNumber);
	
	@Query("SELECT new com.urbanproperty.dto.UserMonthlyStats(" +
		       "MONTH(u.createdTime), " +
		       "u.role, " +
		       "COUNT(u.id)) " +
		       "FROM UserEntity u " +
		       "WHERE u.role IN ('BUYER', 'SELLER') " +
		       "AND YEAR(u.createdTime) = :currentYear " +
		       "GROUP BY MONTH(u.createdTime), u.role " +
		       "ORDER BY MONTH(u.createdTime)")
		List<UserMonthlyStats> findMonthlyUserRegistrationStatsForYear(@Param("currentYear") int currentYear);
}
