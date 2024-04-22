package com.webproject.ecommerce.dto;
import com.webproject.ecommerce.entities.ProductOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private String email;
    private List<ProductOrder> orders;
    private String message;
}
