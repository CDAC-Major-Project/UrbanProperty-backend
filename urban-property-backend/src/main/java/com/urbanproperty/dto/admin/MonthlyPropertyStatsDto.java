package com.urbanproperty.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MonthlyPropertyStatsDto {
    private int month;
    private long count;
}