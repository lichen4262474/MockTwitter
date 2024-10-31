package com.cooksystem.socialmedia.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksystem.socialmedia.dto.TweetRequestDto;
import com.cooksystem.socialmedia.dto.TweetResponseDto;
import com.cooksystem.socialmedia.entity.Tweet;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TweetMapper {

	TweetResponseDto entityToDto(Tweet entity);

	Tweet dtoToEntity(TweetRequestDto tweetRequestDto);

	List<TweetResponseDto> entitiesToDtos(List<Tweet> entities);

}