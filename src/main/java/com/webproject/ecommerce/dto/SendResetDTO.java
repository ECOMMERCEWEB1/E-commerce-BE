package com.webproject.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendResetDTO {
private String email;
    public SendResetDTO() {}

    public SendResetDTO(String email){
    this.email = email;
}
}
