package com.cooksystem.socialmedia.service.interfaces;

import com.cooksystem.socialmedia.dto.CredentialsDto;
import com.cooksystem.socialmedia.dto.TweetResponseDto;
import com.cooksystem.socialmedia.dto.UserRequestDto;
import com.cooksystem.socialmedia.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    List<UserResponseDto> getAllActiveUsers();

    UserResponseDto createUser(UserRequestDto userRequestDto);

    UserResponseDto getUserByUsername(String username);

    UserResponseDto updateUserByUsername(String username, UserRequestDto userRequestDto);

    UserResponseDto deleteUserByUsername(String username, CredentialsDto credentialsDto);

    void followUserByUsername(String username, CredentialsDto credentialsDto);

    void unfollowUserByUsername(String username, CredentialsDto credentialsDto);

    List<TweetResponseDto> getAllTweetsByUsername(String username);

    List<TweetResponseDto> getAllTweetsUserMentioned(String username);

    List<UserResponseDto> getAllFollowersOfUsername(String username);

    List<UserResponseDto> getAllUsersFollowedByUsername(String username);

    List<TweetResponseDto> getAllTweetsByAuthorAndByAuthorFollows(String username);
}
