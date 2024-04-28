package com.webproject.ecommerce.mappers;

import com.webproject.ecommerce.dto.UserDTO;
import com.webproject.ecommerce.entities.User;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDtoAdmin(User user, String message);
    @Mapping(target = "enabled", ignore = true)
    UserDTO toDto(User user, String message);
}
