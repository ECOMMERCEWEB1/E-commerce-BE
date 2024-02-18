package com.webproject.ecommerce.dto;

import com.webproject.ecommerce.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private User user;
    private String message;

}
