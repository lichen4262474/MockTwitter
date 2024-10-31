package com.cooksystem.socialmedia.mapper;

import com.cooksystem.socialmedia.dto.CredentialsDto;
import com.cooksystem.socialmedia.entity.Credentials;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {
    CredentialsDto entityToDto(Credentials entity);

    Credentials dtoToEntity(CredentialsDto credentialsDto);
}