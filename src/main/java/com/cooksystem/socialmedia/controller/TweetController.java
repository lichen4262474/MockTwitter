package com.cooksystem.socialmedia.controller;

import com.cooksystem.socialmedia.dto.*;
import com.cooksystem.socialmedia.service.interfaces.TweetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {
    private final TweetService tweetService;

    @GetMapping
    public List<TweetResponseDto> getAllTweets(){
        return tweetService.getAllTweets();
    }

    @PostMapping
    public TweetResponseDto createTweet(@Valid @RequestBody TweetRequestDto tweetRequestDto){
        return tweetService.createTweet(tweetRequestDto);
    }

    @GetMapping("/{id}")
    public TweetResponseDto getTweetById(@PathVariable Long id){
        return tweetService.getTweetById(id);
    }

    @DeleteMapping("/{id}")
    public TweetResponseDto deleteTweetByID(@PathVariable Long id, @Valid@RequestBody CredentialsDto credentialsDto){
        return tweetService.deleteTweetById(id,credentialsDto);
    }

    @PostMapping("/{id}/like")
    public void createLike(@PathVariable Long id,@Valid@RequestBody CredentialsDto credentialsDto){
          tweetService.createLike(id,credentialsDto);
    }

    @PostMapping("/{id}/reply")
    public TweetResponseDto replyToTweet(@PathVariable Long id, @Valid@RequestBody TweetRequestDto tweetRequestDto){
        return tweetService.replyToTweet(id,tweetRequestDto);
    }

    @PostMapping("{id}/repost")
    public TweetResponseDto repostTweet(@PathVariable Long id, @Valid@RequestBody CredentialsDto credentialsDto){
        return tweetService.repostTweet(id,credentialsDto);
    }

    @GetMapping("/{id}/tags")
    public List<HashtagDto> getHashtagsByTweetId(@PathVariable Long id){
        return tweetService.getHashtagsByTweetId(id);
    }

    @GetMapping("/{id}/likes")
    public List<UserResponseDto> getUsersLikeTheTweet(@PathVariable Long id){
        return tweetService.getUsersLikeTheTweet(id);
    }

    @GetMapping("/{id}/context")
    public ContextDto getContextOfTweet(@PathVariable Long id){
        return tweetService.getContextOfTweet(id);
    }

    @GetMapping("/{id}/replies")
    public List<TweetResponseDto> getRepliesToTweet(@PathVariable Long id){
        return tweetService.getRepliesToTweet(id);
    }

    @GetMapping("/{id}/reposts")
    public List<TweetResponseDto> getRepostsOfTweet(@PathVariable Long id){
        return tweetService.getRepostsOfTweet(id);
    }

    @GetMapping("/{id}/mentions")
    public List<UserResponseDto> getUsersMentionedInTweet(@PathVariable Long id){
        return tweetService.getUsersMentionedInTweet(id);
    }

}
