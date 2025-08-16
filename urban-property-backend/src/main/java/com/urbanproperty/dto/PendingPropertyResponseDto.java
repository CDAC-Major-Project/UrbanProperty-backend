package com.urbanproperty.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PendingPropertyResponseDto {

    private Long id;
    private String title;
    private String address;
    private BigDecimal startingPrice;
    private LocalDateTime createdTime;

    private String sellerName;
    private String propertyTypeName;
}