package com.cooksystem.socialmedia.service.impl;

import com.cooksystem.socialmedia.dto.HashtagDto;
import com.cooksystem.socialmedia.dto.TweetResponseDto;
import com.cooksystem.socialmedia.entity.Hashtag;
import com.cooksystem.socialmedia.entity.Tweet;
import com.cooksystem.socialmedia.exceptions.NotFoundException;
import com.cooksystem.socialmedia.mapper.HashtagMapper;
import com.cooksystem.socialmedia.mapper.TweetMapper;
import com.cooksystem.socialmedia.repository.HashtagRepo;
import com.cooksystem.socialmedia.repository.TweetRepo;
import com.cooksystem.socialmedia.repository.UserRepo;
import com.cooksystem.socialmedia.service.interfaces.HashtagService;
import com.cooksystem.socialmedia.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HashtagServiceImp implements HashtagService {
    private final UserRepo userRepo;
    private final TweetRepo tweetRepo;
    private final HashtagRepo hashtagRepo;
    private final HashtagMapper hashtagMapper;
    private final TweetMapper tweetMapper;

    @Override
    public List<HashtagDto> getAllHashtags() {
        return hashtagMapper.entitiesToDtos(hashtagRepo.findAll());
    }

    @Override
    public List<TweetResponseDto> getTweetsWithLabel(String label) {
        Hashtag hashtag = hashtagRepo.findByLabel(label).orElseThrow(()->new NotFoundException("Hashtag with label "+label+"is not found"));

        List<Tweet> activeTweetList = hashtag.getTweets().stream()
                .filter(tweet -> !tweet.isDeleted())
                .sorted((t1, t2) -> t2.getPosted().compareTo(t1.getPosted()))
                .collect(Collectors.toList());
        return activeTweetList.stream()
                .map(tweet -> tweetMapper.entityToDto(tweet))
                .collect(Collectors.toList());
    }
}