package com.cooksystem.socialmedia.mapper;

import com.cooksystem.socialmedia.dto.UserRequestDto;
import com.cooksystem.socialmedia.dto.UserResponseDto;
import com.cooksystem.socialmedia.entity.User;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProfileMapper.class, CredentialsMapper.class})
public interface UserMapper {

	@Mapping(target = "username", source = "credentials.username")
    UserResponseDto entityToDto(User user);

    User dtoToEntity(UserRequestDto userRequestDto);

    List<UserResponseDto> entitiesToDtos(List<User> users);
}
