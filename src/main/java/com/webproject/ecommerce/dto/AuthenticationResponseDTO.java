package com.webproject.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthenticationResponseDTO {
    // PROBABLY GOING TO BE SET TO COOKIES... [ FOR NOW KEPT AS RESPONSE ]
    private String token;
    private String message;
}
