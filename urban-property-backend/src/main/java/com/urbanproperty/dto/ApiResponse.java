package com.urbanproperty.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse {
	private LocalDateTime timeStamp;
	private String message;

	public ApiResponse(String message) {
		this.timeStamp = LocalDateTime.now();
		this.message = message;
	}

	/**
	 * A generic DTO for sending simple, consistent API responses,
	 * especially for error messages or success notifications.
	 */

}
