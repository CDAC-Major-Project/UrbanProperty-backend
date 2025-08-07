// File: src/main/java/com/urbanproperty/dto/UserMonthlyStats.java
package com.urbanproperty.dto.admin;

import com.urbanproperty.entities.Role;

public class UserMonthlyStats {
    private int month;
    private Role role;
    private long count;

    public UserMonthlyStats(int month, Role role, long count) {
        this.month = month;
        this.role = role;
        this.count = count;
    }

    // Getters
    public int getMonth() { return month; }
    public Role getRole() { return role; }
    public long getCount() { return count; }
}