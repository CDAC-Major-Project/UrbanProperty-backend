package com.urbanproperty.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyDetailsDto {
    private Integer numBedrooms;
    private Integer numBathrooms;
    private Integer sizeSqft;
    private Integer buildYear;
}