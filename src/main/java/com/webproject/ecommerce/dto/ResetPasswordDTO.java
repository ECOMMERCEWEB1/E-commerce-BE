package com.webproject.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class ResetPasswordDTO {
    private String token;
    private String password;
    private Long id;
}
