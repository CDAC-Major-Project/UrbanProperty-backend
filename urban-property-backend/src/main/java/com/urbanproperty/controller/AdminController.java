package com.urbanproperty.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.urbanproperty.dto.admin.AdminDashboardStatsDTO;
import com.urbanproperty.entities.PropertyStatus;
import com.urbanproperty.service.PropertyService;
import com.urbanproperty.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin")
@AllArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // Secures all endpoints in this controller
public class AdminController {

    private final PropertyService propertyService;
    private final UserService userService;

    @Operation(summary = "Get counts of properties grouped by status (Admin Only)")
    @GetMapping("/dashboard/property-status-counts")
    public ResponseEntity<Map<PropertyStatus, Long>> getPropertyStatusCounts() {
        Map<PropertyStatus, Long> statusCounts = propertyService.getPropertyStatusCounts();
        return ResponseEntity.ok(statusCounts);
    }
    
    @GetMapping("/dashboard/seller-buyer-count")
	@Operation(
	    summary = "Get Admin Dashboard Statistics (Admin Only)",
	    description = "Provides statistics for the current year, including total user count and a monthly breakdown of new buyers and sellers."
	)
	public ResponseEntity<AdminDashboardStatsDTO> getAdminDashboardStats() {
	    AdminDashboardStatsDTO stats = userService.getDashboardStatistics();
	    return ResponseEntity.ok(stats);
	}
    
    @Operation(summary = "Get monthly property listing stats for the current year (Admin Only)")
    @GetMapping("/dashboard/monthly-property-stats")
    public ResponseEntity<Map<String, Long>> getMonthlyPropertyStats() {
        Map<String, Long> monthlyStats = propertyService.getMonthlyPropertyStatsForCurrentYear();
        return ResponseEntity.ok(monthlyStats);
    }
}