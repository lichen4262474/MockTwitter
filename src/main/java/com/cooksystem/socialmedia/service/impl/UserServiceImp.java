package com.cooksystem.socialmedia.service.impl;

import com.cooksystem.socialmedia.dto.*;
import com.cooksystem.socialmedia.entity.Credentials;
import com.cooksystem.socialmedia.entity.Profile;
import com.cooksystem.socialmedia.entity.Tweet;
import com.cooksystem.socialmedia.entity.User;
import com.cooksystem.socialmedia.exceptions.BadRequestException;
import com.cooksystem.socialmedia.exceptions.NotAuthorizedException;
import com.cooksystem.socialmedia.exceptions.NotFoundException;
import com.cooksystem.socialmedia.mapper.CredentialsMapper;
import com.cooksystem.socialmedia.mapper.ProfileMapper;
import com.cooksystem.socialmedia.mapper.TweetMapper;
import com.cooksystem.socialmedia.mapper.UserMapper;
import com.cooksystem.socialmedia.repository.TweetRepo;
import com.cooksystem.socialmedia.repository.UserRepo;
import com.cooksystem.socialmedia.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final ProfileMapper profileMapper;
    private final CredentialsMapper credentialsMapper;
    private final TweetRepo tweetRepo;
    private final TweetMapper tweetMapper;

    @Override
    public List<UserResponseDto> getAllActiveUsers() {
        List<User> userList = userRepo.findByDeletedFalse();
        return userMapper.entitiesToDtos(userList);
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        Credentials credentials = credentialsMapper.dtoToEntity(userRequestDto.getCredentials());
        Profile profile = profileMapper.dtoToEntity(userRequestDto.getProfile());
        System.out.println("Credentials and Profile in service implementation have been created  " + credentials.getUsername());
        Optional<User> existingUserOptional = userRepo.findByCredentials(credentials);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            if (existingUser.isDeleted()) {
                existingUser.setDeleted(false);
                existingUser.setProfile(profile);
                userRepo.save(existingUser);
                return userMapper.entityToDto(existingUser);
            } else {
                throw new BadRequestException("Username already used");
            }
        }
        User newUser = userMapper.dtoToEntity(userRequestDto);
        userRepo.save(newUser);
        return userMapper.entityToDto(newUser);
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        User user = userRepo.findByCredentialsUsername(username).orElseThrow(() -> new NotFoundException("User with username " + username + " not found"));
        System.out.println("User has been found with name " + username);
        if (user.isDeleted()) {
            throw new NotFoundException("User is deleted");
        }
        return userMapper.entityToDto(user);
    }

    @Override
    public UserResponseDto updateUserByUsername(String username,UserRequestDto userRequestDto) {
        User user = userRepo.findByCredentialsUsername(username)
                .orElseThrow(() -> new NotFoundException("User with username " + username + " not found"));

        if (user.isDeleted()) {
            throw new BadRequestException("User is deleted");
        }
        if (userRequestDto.getCredentials() == null) {
            throw new BadRequestException("Credentials cannot be null");
        }

        if (userRequestDto.getProfile() == null) {
            throw new BadRequestException("Profile cannot be null");
        }

        Profile requestProfile = profileMapper.dtoToEntity(userRequestDto.getProfile());
        Credentials credentials = credentialsMapper.dtoToEntity(userRequestDto.getCredentials());

        if (!user.getCredentials().equals(credentials)) {
            throw new NotAuthorizedException("You are not authorized to update the profile for user " + username);
        }

        if (requestProfile != null) {
            if (requestProfile.getFirstName() != null) {
                user.getProfile().setFirstName(requestProfile.getFirstName());
            }
            if (requestProfile.getLastName() != null) {
                user.getProfile().setLastName(requestProfile.getLastName());
            }
            if (requestProfile.getEmail() != null) {
                user.getProfile().setEmail(requestProfile.getEmail());
            }
            if (requestProfile.getPhone() != null) {
                user.getProfile().setPhone(requestProfile.getPhone());
            }
        }
        userRepo.save(user);
        return userMapper.entityToDto(user);
    }

    @Override
    public UserResponseDto deleteUserByUsername(String username, CredentialsDto credentialsDto) {
        User user = userRepo.findByCredentialsUsername(username).orElseThrow(() -> new NotFoundException("User with username " + username + " not found"));
        if (user.isDeleted()) {
            throw new NotFoundException("User named "+username+" is already deleted");
        }
        if(user.getCredentials().equals(credentialsMapper.dtoToEntity(credentialsDto))){
             user.setDeleted(true);
             userRepo.save(user);
        }
        return userMapper.entityToDto(user);
    }

    @Override
    public void followUserByUsername(String username, CredentialsDto credentialsDto) {
        User followee = userRepo.findByCredentialsUsername(username).orElseThrow(() -> new NotFoundException("Followee with username " + username + " not found"));
        if (followee.isDeleted()) {
            throw new NotFoundException("Followee named "+username+" is already deleted");
        }
        User follower = userRepo.getUserByCredentials(credentialsMapper.dtoToEntity(credentialsDto)).orElseThrow(()->new NotAuthorizedException("You are not authorized for the following action"));
        if(follower.getFollowings().contains(followee)){
            throw new BadRequestException("You have already followed " + username);
        }
        follower.follow(followee);
        followee.addFollower(follower);
        userRepo.save(followee);
        userRepo.save(follower);
    }

    @Override
    public void unfollowUserByUsername(String username, CredentialsDto credentialsDto) {
        User followee = userRepo.findByCredentialsUsername(username).orElseThrow(() -> new NotFoundException("Followee with username " + username + " not found"));
        if (followee.isDeleted()) {
            throw new NotFoundException("Followee named "+username+" is already deleted");
        }
        User follower = userRepo.getUserByCredentials(credentialsMapper.dtoToEntity(credentialsDto)).orElseThrow(()->new NotAuthorizedException("You are not authorized for the following action"));
        if(!follower.getFollowings().contains(followee)){
            throw new BadRequestException("You did not follow " + username);
        }
        follower.unfollow(followee);
        followee.deleteFollower(follower);
        userRepo.save(followee);
        userRepo.save(follower);
    }

    @Override
    public List<TweetResponseDto> getAllTweetsByUsername(String username) {
        User user = userRepo.findByCredentialsUsername(username).orElseThrow(() -> new NotFoundException("User with username " + username + " not found"));
        List<Tweet> tweetList = tweetRepo.findByAuthorAndDeletedFalse(user);
        tweetList.sort((t1, t2) -> t2.getPosted().compareTo(t1.getPosted()));
        return tweetMapper.entitiesToDtos(tweetList);
    }

    @Override
    public List<TweetResponseDto> getAllTweetsUserMentioned(String username) {
        User user = userRepo.findByCredentialsUsername(username).orElseThrow(() -> new NotFoundException("User with username " + username + " not found"));
        List<Tweet> tweetList = tweetRepo.findAllByMentionedUsersContains(user);
        tweetList.sort((t1, t2) -> t2.getPosted().compareTo(t1.getPosted()));
        return tweetMapper.entitiesToDtos(tweetList);
    }

    @Override
    public List<UserResponseDto> getAllFollowersOfUsername(String username) {
        User user = userRepo.findByCredentialsUsername(username).orElseThrow(() -> new NotFoundException("User with username " + username + " not found"));
        List<User> followers = user.getFollowers().stream().filter(u->!u.isDeleted()).collect(Collectors.toList());
        return userMapper.entitiesToDtos(followers);
    }

    @Override
    public List<UserResponseDto> getAllUsersFollowedByUsername(String username) {
        User user = userRepo.findByCredentialsUsername(username).orElseThrow(() -> new NotFoundException("User with username " + username + " not found"));
        List<User> followings = user.getFollowings().stream().filter(u->!u.isDeleted()).collect(Collectors.toList());
        return userMapper.entitiesToDtos(followings);
    }

    @Override
    public List<TweetResponseDto> getAllTweetsByAuthorAndByAuthorFollows(String username) {
        User user = userRepo.findByCredentialsUsername(username)
                .orElseThrow(() -> new NotFoundException("User with username " + username + " not found"));

        // Fetching non-deleted tweets by the author
        List<Tweet> tweetsByAuthor = user.getTweets().stream()
                .filter(t -> !t.isDeleted()) // Include only non-deleted tweets
                .collect(Collectors.toList());

        // Fetching non-deleted tweets from followings
        List<Tweet> tweetsByFollowings = user.getFollowings().stream()
                .flatMap(following -> following.getTweets().stream()
                        .filter(t -> !t.isDeleted())) // Include only non-deleted tweets
                .collect(Collectors.toList());

        // Combining the two lists
        List<Tweet> combinedList = new ArrayList<>(tweetsByAuthor);
        combinedList.addAll(tweetsByFollowings);

        //  Sorting combined list by posted timestamp if required
        combinedList.sort(Comparator.comparing(Tweet::getPosted).reversed()); // Sort by posted timestamp in descending order

        return tweetMapper.entitiesToDtos(combinedList);
    }


}
