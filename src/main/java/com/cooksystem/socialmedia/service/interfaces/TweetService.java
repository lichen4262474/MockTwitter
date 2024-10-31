package com.cooksystem.socialmedia.service.interfaces;

import com.cooksystem.socialmedia.dto.*;
import com.cooksystem.socialmedia.entity.Credentials;

import java.util.List;

public interface TweetService {
    List<TweetResponseDto> getAllTweets();

    TweetResponseDto createTweet(TweetRequestDto tweetRequestDto);

    TweetResponseDto getTweetById(Long id);

    TweetResponseDto deleteTweetById(Long id, CredentialsDto credentialsDto);

    void createLike(Long id, CredentialsDto credentialsDto);

    TweetResponseDto replyToTweet(Long id, TweetRequestDto tweetRequestDto);

    TweetResponseDto repostTweet(Long id, CredentialsDto credentialsDto);

    List<HashtagDto> getHashtagsByTweetId(Long id);

    List<UserResponseDto> getUsersLikeTheTweet(Long id);

    ContextDto getContextOfTweet(Long id);

    List<TweetResponseDto> getRepliesToTweet(Long id);

    List<TweetResponseDto> getRepostsOfTweet(Long id);

    List<UserResponseDto> getUsersMentionedInTweet(Long id);
}
