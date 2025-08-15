package com.urbanproperty.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MonthlyPropertyStatsDto {
    private int month;
    private long count;
}