package com.cooksystem.socialmedia.controller;

import com.cooksystem.socialmedia.dto.HashtagDto;
import com.cooksystem.socialmedia.dto.TweetResponseDto;
import com.cooksystem.socialmedia.service.interfaces.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class HashTagController {
    private final HashtagService hashtagService;
    @GetMapping
    public List<HashtagDto> getAllHashtags(){
        return hashtagService.getAllHashtags();
    }
    //Retrieves all (non-deleted) tweets tagged with the given hashtag label
    @GetMapping("/{label}")
    public List<TweetResponseDto> getTweetsWithLabel(@PathVariable String label){
        return hashtagService.getTweetsWithLabel(label);
    }


}
