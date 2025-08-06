package com.urbanproperty.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for the structured response of the getAllUsers endpoint.
 * It separates users into lists based on their role.
 */
@Getter
@Setter
public class AllUsersResponse {
    private List<UserResponse> seller;
    private List<UserResponse> buyer;
}
