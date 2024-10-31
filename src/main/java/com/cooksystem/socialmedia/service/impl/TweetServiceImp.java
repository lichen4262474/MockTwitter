package com.cooksystem.socialmedia.service.impl;

import com.cooksystem.socialmedia.dto.*;
import com.cooksystem.socialmedia.entity.Credentials;
import com.cooksystem.socialmedia.entity.Hashtag;
import com.cooksystem.socialmedia.entity.Tweet;
import com.cooksystem.socialmedia.entity.User;
import com.cooksystem.socialmedia.exceptions.BadRequestException;
import com.cooksystem.socialmedia.exceptions.NotAuthorizedException;
import com.cooksystem.socialmedia.exceptions.NotFoundException;
import com.cooksystem.socialmedia.mapper.CredentialsMapper;
import com.cooksystem.socialmedia.mapper.HashtagMapper;
import com.cooksystem.socialmedia.mapper.TweetMapper;
import com.cooksystem.socialmedia.mapper.UserMapper;
import com.cooksystem.socialmedia.repository.HashtagRepo;
import com.cooksystem.socialmedia.repository.TweetRepo;
import com.cooksystem.socialmedia.repository.UserRepo;
import com.cooksystem.socialmedia.service.interfaces.TweetService;
import com.cooksystem.socialmedia.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TweetServiceImp implements TweetService {
    private final UserRepo userRepo;
    private final TweetRepo tweetRepo;
    private final HashtagRepo hashtagRepo;
    private final TweetMapper tweetMapper;
    private final CredentialsMapper credentialsMapper;
    private final HashtagMapper hashtagMapper;
    private final UserMapper userMapper;

    //helper method to process tweet content
    //add tweets to hashtags, add hashtag to the tweet, persist hashtag
    private void processMentionsAndHashtags(String content, Tweet tweet) {
        // Split the content into words
        String[] words = content.split("\\s+");  // Split by whitespace

        for (String word : words) {
            if (word.startsWith("@")) {
                // Extract the username after the @ symbol
                String username = word.substring(1);

                // Look up the mentioned user in the database
                User mentionedUser = userRepo.findByCredentialsUsername(username)
                        .orElseThrow(() -> new BadRequestException("Mentioned user @" + username + " does not exist."));

                // Add the mentioned user to the tweet's mentionedUsers list and add the tweet to the mentioned user's MentionedList
                tweet.addMentionedUser(mentionedUser);
                mentionedUser.addMentionedTweet(tweet);

            } else if (word.startsWith("#")) {
                // Extract the hashtag after the # symbol
                String hashtagLabel = word.substring(1);

                // Check if the hashtag exists in the database, otherwise create it
                Hashtag hashtag = hashtagRepo.findByLabel(hashtagLabel)
                        .orElseGet(()-> new Hashtag());
                //add tweet to hashtag's the tweets list
                hashtag.setLabel(hashtagLabel);
                hashtag.addTweetToHashtag(tweet);
                // Add the hashtag to the tweet's hashtags list
                tweet.addHashtags(hashtag);
                hashtagRepo.save(hashtag);  // Save the new hashtag
            }
        }
    }

    @Override
    public List<TweetResponseDto> getAllTweets() {
        List<Tweet> tweetList = tweetRepo.findByDeletedFalse();
        return tweetMapper.entitiesToDtos(tweetList);
    }

    @Override
    public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {
        Credentials credentials =  credentialsMapper.dtoToEntity(tweetRequestDto.getCredentials());
        User user = userRepo.getUserByCredentials(credentials).orElseThrow(()->new BadRequestException("Credentials not valid"));
        if(user.isDeleted()){
            throw new BadRequestException("This user is deleted");
        }
        Tweet tweet = tweetMapper.dtoToEntity(tweetRequestDto);
//        System.out.println("Tweet has been created!");

        tweet.setAuthor(user);
//        System.out.println("Tweet's author has been set " + tweet.getAuthor().getId());
        String content = tweet.getContent();

        //add tweets to hashtags and add hashtag to the tweet
        processMentionsAndHashtags(content,tweet);
        user.addTweet(tweet);
        tweetRepo.save(tweet);
        userRepo.save(user);
        return tweetMapper.entityToDto(tweet);
    }

    @Override
    public TweetResponseDto getTweetById(Long id) {
        Tweet tweet = tweetRepo.findById(id).orElseThrow(()->new NotFoundException("The id is not valid"));
        return tweetMapper.entityToDto(tweet);
    }

    @Override
    public TweetResponseDto deleteTweetById(Long id, CredentialsDto credentialsDto) {
        Tweet tweet = tweetRepo.findById(id).orElseThrow(()->new NotFoundException("The id is not valid"));
        Credentials  credentials= credentialsMapper.dtoToEntity(credentialsDto);
        if(!tweet.getAuthor().getCredentials().equals(credentials)){
            throw new NotAuthorizedException("The credentials are not valid");
        }
        if(tweet.isDeleted()){
            throw new BadRequestException("The tweet has been deleted already");
        }
        tweet.setDeleted(true);
        tweetRepo.save(tweet);
        return tweetMapper.entityToDto(tweet);
    }

    @Override
    public void createLike(Long id, CredentialsDto credentialsDto) {
        Tweet tweet = tweetRepo.findById(id).orElseThrow(()->new NotFoundException("The id is not valid"));
        User user = userRepo.getUserByCredentials(credentialsMapper.dtoToEntity(credentialsDto)).orElseThrow(()->new NotAuthorizedException("The credentials are not valid"));
        if(user.isDeleted()){
            throw new NotAuthorizedException("User is deleted");
        }
        tweet.addUserToLikesList(user);
        user.addMentionedTweet(tweet);
        userRepo.save(user);
        tweetRepo.save(tweet);
    }

    @Override
    public TweetResponseDto replyToTweet(Long id, TweetRequestDto tweetRequestDto) {
        // Check if the tweet being replied to exists and is not deleted
        Tweet replyTo = tweetRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("The id is not valid"));

        if (replyTo.isDeleted()) {
            throw new NotFoundException("Cannot reply to a deleted tweet.");
        }

        String content = tweetRequestDto.getContent();

        // Check if content is not empty
        if (content == null || content.trim().isEmpty()) {
            throw new BadRequestException("Tweet content cannot be empty.");
        }

        // Map credentials and retrieve the user
        Credentials credentials = credentialsMapper.dtoToEntity(tweetRequestDto.getCredentials());
        User user = userRepo.getUserByCredentials(credentials)
                .orElseThrow(() -> new NotAuthorizedException("The credentials are not valid"));

        // Check if the user is deleted
        if (user.isDeleted()) {
            throw new NotAuthorizedException("User is deleted");
        }

        // Create and process the reply tweet
        Tweet tweet = new Tweet();
        tweet.setAuthor(user);
        tweet.setInReplyTo(replyTo);
        tweet.setContent(content); // Ensure the content is set before processing

        // Process mentions and hashtags
        processMentionsAndHashtags(content, tweet);

        // Save the reply tweet
        tweetRepo.save(tweet);

        // Add the reply to the original tweet's replies list
        replyTo.addReplyToRepliesList(tweet);

        // Save the original tweet after updating its replies
        tweetRepo.save(replyTo);

        return tweetMapper.entityToDto(tweet);
    }

    @Override
    public TweetResponseDto repostTweet(Long id, CredentialsDto credentialsDto) {
        Tweet tweet = tweetRepo.findById(id).orElseThrow(()->new NotFoundException("The id is not valid"));
        if(tweet.isDeleted()){
            throw new NotFoundException("The tweet is deleted");
        }
        Credentials credentials= credentialsMapper.dtoToEntity(credentialsDto);
        User user = userRepo.getUserByCredentials(credentials).orElseThrow(()->new NotAuthorizedException("The credentials are not valid"));
        if(user.isDeleted()){
            throw new NotAuthorizedException("User with the credentials is deleted");
        }
        String content = tweet.getContent();
        Tweet newTweet = new Tweet();
        newTweet.setAuthor(user);
        newTweet.setContent(content);
        processMentionsAndHashtags(content,tweet);
        newTweet.setRepostOf(tweet);
        tweet.addRepostToRepostsList(tweet);
        tweetRepo.save(newTweet);
        tweetRepo.save(tweet);
        return tweetMapper.entityToDto(newTweet);
    }

    @Override
    public List<HashtagDto> getHashtagsByTweetId(Long id) {
        Tweet tweet = tweetRepo.findById(id).orElseThrow(()->new NotFoundException("The id is not valid"));
        if(tweet.isDeleted()){
            throw new NotFoundException("The tweet is deleted");
        }
        List<Hashtag> hashtags = tweet.getHashtags();
        return hashtagMapper.entitiesToDtos(hashtags);
    }

    @Override
    public List<UserResponseDto> getUsersLikeTheTweet(Long id) {
        Tweet tweet = tweetRepo.findById(id).orElseThrow(()->new NotFoundException("The id is not valid"));
        if(tweet.isDeleted()){
            throw new NotFoundException("The tweet is deleted");
        }
        List<User> users = tweet.getUsersLike().stream().filter(u->!u.isDeleted()).collect(Collectors.toList());
        return userMapper.entitiesToDtos(users);
    }

    @Override
    public ContextDto getContextOfTweet(Long id) {
        // Find the target tweet
        Tweet targetTweet = tweetRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("The tweet with the provided id is not valid"));

        // Check if the target tweet is deleted
        if (targetTweet.isDeleted()) {
            throw new NotFoundException("The tweet is deleted");
        }

        // Get all replies including nested replies
        List<Tweet> chainOfReplies = targetTweet.getChainOfReplies();
        List<Tweet> chainOfParent = targetTweet.getChainOfInReplyTo();
        // Create a DTO to hold context information
        ContextDto contextDto = new ContextDto();
        contextDto.setTarget(tweetMapper.entityToDto(targetTweet));
        contextDto.setBefore(tweetMapper.entitiesToDtos(chainOfParent));
        contextDto.setAfter(tweetMapper.entitiesToDtos(chainOfReplies));

        return contextDto;
    }


    @Override
    public List<TweetResponseDto> getRepliesToTweet(Long id) {
        Tweet targetTweet = tweetRepo.findById(id).orElseThrow(()->new NotFoundException("The id is not valid"));
        if(targetTweet.isDeleted()){
            throw new NotFoundException("The tweet is deleted");
        }
        List<Tweet> replies = targetTweet.getReplies().stream().filter(r-> !r.isDeleted()).collect(Collectors.toList());

        return tweetMapper.entitiesToDtos(replies);
    }

    @Override
    public List<TweetResponseDto> getRepostsOfTweet(Long id) {
        Tweet tweet = tweetRepo.findById(id).orElseThrow(()->new NotFoundException("The id is not valid"));
        if(tweet.isDeleted()){
            throw new NotFoundException("The tweet is deleted");
        }
        List<Tweet> reposts = tweet.getReposts().stream().filter(r->!r.isDeleted()).collect(Collectors.toList());
        return tweetMapper.entitiesToDtos(reposts);
    }

    @Override
    public List<UserResponseDto> getUsersMentionedInTweet(Long id) {
        Tweet tweet = tweetRepo.findById(id).orElseThrow(()->new NotFoundException("The id is not valid"));
        if(tweet.isDeleted()){
            throw new NotFoundException("The tweet is deleted");
        }
        List<User> mentionedUser = tweet.getMentionedUsers().stream().filter(u->!u.isDeleted()).collect(Collectors.toList());
        return userMapper.entitiesToDtos(mentionedUser);
    }


}