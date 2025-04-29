package com.tinkoff_lab.mapper;

import com.tinkoff_lab.dto.n.UserDTO;
import com.tinkoff_lab.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User dtoToEntity(UserDTO userDTO);

    UserDTO entityToDto(User user);

    List<UserDTO> entitiesToDto(List<User> users);
}
