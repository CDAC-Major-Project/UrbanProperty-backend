package com.urbanproperty.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyTypeWithCountDto {
	@JsonProperty(access =Access.READ_ONLY)
	private Long id ;
	@NotBlank
	private String name;
	private String description;
	private LocalDateTime createdTime;
	private long count; // The new field
}
