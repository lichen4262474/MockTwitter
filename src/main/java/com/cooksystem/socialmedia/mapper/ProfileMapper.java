package com.cooksystem.socialmedia.mapper;

import com.cooksystem.socialmedia.dto.ProfileDto;
import com.cooksystem.socialmedia.entity.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileDto entityToDto(Profile entity);

    Profile dtoToEntity(ProfileDto profileDto);

}
