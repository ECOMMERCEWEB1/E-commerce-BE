package com.webproject.ecommerce.mappers;

import com.webproject.ecommerce.dto.UserDTO;
import com.webproject.ecommerce.entities.User;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDto(User user, @Context String message);

    //User toEntity(UserDTO userDTO);
}
