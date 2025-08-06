package com.urbanproperty.dto;

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
	
	  private Long id;
	  
	  @NotBlank(message = "Name cannot be blank")
	  private String name;
	  
	  private  String iconUrl;
}
