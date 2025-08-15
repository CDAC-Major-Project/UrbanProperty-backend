package com.urbanproperty.dto.admin;

import com.urbanproperty.entities.PropertyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor // This constructor will be used by JPA
public class PropertyStatusCountDto {
    private PropertyStatus status;
    private long count;
}