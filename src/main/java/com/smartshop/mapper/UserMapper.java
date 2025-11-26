package com.smartshop.mapper;

import com.smartshop.dto.request.UserCreateDTO;
import com.smartshop.dto.response.UserResponseDTO;
import com.smartshop.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserCreateDTO dto);
    UserResponseDTO toDTO(User user);
    List<UserResponseDTO> toListDTO(List<User> users);
}
