package com.webproject.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignUpDTO {
    private String firstName;
    private String lastName;
    private Integer age;
    private String email;
    private String password;
    // ADD AVATAR
}
