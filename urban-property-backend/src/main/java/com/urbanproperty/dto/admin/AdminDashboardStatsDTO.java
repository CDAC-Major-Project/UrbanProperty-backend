// File: src/main/java/com/urbanproperty/dto/AdminDashboardStatsDTO.java
package com.urbanproperty.dto.admin;

import java.util.List;

public class AdminDashboardStatsDTO {
    private long totalUsers;
    private List<MonthlyUserStatsDTO> monthlyData;

    public AdminDashboardStatsDTO(long totalUsers, List<MonthlyUserStatsDTO> monthlyData) {
        this.totalUsers = totalUsers;
        this.monthlyData = monthlyData;
    }

    // Getters and Setters
    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
    public List<MonthlyUserStatsDTO> getMonthlyData() { return monthlyData; }
    public void setMonthlyData(List<MonthlyUserStatsDTO> monthlyData) { this.monthlyData = monthlyData; }
}