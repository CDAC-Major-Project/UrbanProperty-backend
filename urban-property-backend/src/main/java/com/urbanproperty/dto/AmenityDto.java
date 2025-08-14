package com.urbanproperty.dto;

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

public class AmenityDto {
	@JsonProperty(access =Access.READ_ONLY)
	  private Long id;
	  
	  @NotBlank(message = "Name cannot be blank")
	  private String name;
	  
//	  private  String iconUrl;
}
