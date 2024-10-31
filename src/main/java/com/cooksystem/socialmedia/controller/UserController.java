package com.cooksystem.socialmedia.controller;

import com.cooksystem.socialmedia.dto.CredentialsDto;
import com.cooksystem.socialmedia.dto.TweetResponseDto;
import com.cooksystem.socialmedia.dto.UserRequestDto;
import com.cooksystem.socialmedia.dto.UserResponseDto;
import com.cooksystem.socialmedia.service.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    @GetMapping
    public List<UserResponseDto> getAllActiveUsers(){
        return userService.getAllActiveUsers();
    }

    @PostMapping
    public UserResponseDto createUser(@Validated @RequestBody UserRequestDto userRequestDto){

        System.out.println("UserRequestDto has gone through the validation");
        return userService.createUser(userRequestDto);
    }

    @GetMapping("/@{username}")
    public UserResponseDto getUserByUsername(@PathVariable String username){
        return userService.getUserByUsername(username);
    }

    @PatchMapping("/@{username}")
    public UserResponseDto updateUserByUsername(@PathVariable String username, @RequestBody UserRequestDto userRequestDto){
        return userService.updateUserByUsername(username,userRequestDto);
    }

    @DeleteMapping("/@{username}")
    public UserResponseDto deleteUserByUsername(@PathVariable String username, @Validated@RequestBody CredentialsDto credentialsDto){
        return userService.deleteUserByUsername(username,credentialsDto);
    }

    @PostMapping("/@{username}/follow")
    public void followUserByUsername(@PathVariable String username, @Validated@RequestBody CredentialsDto credentialsDto){
        userService.followUserByUsername(username, credentialsDto);
    }

    @PostMapping("/@{username}/unfollow")
    public void unfollowUserByUsername(@PathVariable String username, @Validated@RequestBody CredentialsDto credentialsDto){
        userService.unfollowUserByUsername(username,credentialsDto);
    }

    @GetMapping("/@{username}/feed")
    public List<TweetResponseDto> getAllTweetsByAuthorAndByAuthorFollows(@PathVariable String username){
        return userService.getAllTweetsByAuthorAndByAuthorFollows(username);
    }

    @GetMapping("/@{username}/tweets")
    public List<TweetResponseDto> getAllTweetsByUsername(@PathVariable String username){
        return userService.getAllTweetsByUsername(username);
    }

    @GetMapping("/@{username}/mentions")
    public List<TweetResponseDto> getAllTweetsUserMentioned(@PathVariable String username){
        return userService.getAllTweetsUserMentioned(username);
    }

    @GetMapping("/@{username}/followers")
    public List<UserResponseDto> getAllFollowersOfUsername(@PathVariable String username){
        return userService.getAllFollowersOfUsername(username);
    }

    @GetMapping("/@{username}/following")
    public List<UserResponseDto> getAllUserFollowedByUsername(@PathVariable String username){
        return userService.getAllUsersFollowedByUsername(username);
    }
}
