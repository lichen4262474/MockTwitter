package com.cooksystem.socialmedia.mapper;

import com.cooksystem.socialmedia.dto.HashtagDto;
import com.cooksystem.socialmedia.entity.Hashtag;

import java.util.List;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HashtagMapper {

    HashtagDto entityToDto(Hashtag hashtag);

    List<HashtagDto> entitiesToDtos(List<Hashtag> hashtags);

}