package com.urbanproperty.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class PropertyTypeDto {
	@JsonProperty(access =Access.READ_ONLY)
	private Long id ;
	@NotBlank
	private String name;
	private String description;

}
