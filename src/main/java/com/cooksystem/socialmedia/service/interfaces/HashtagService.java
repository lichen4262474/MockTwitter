package com.cooksystem.socialmedia.service.interfaces;

import com.cooksystem.socialmedia.dto.HashtagDto;
import com.cooksystem.socialmedia.dto.TweetResponseDto;
import com.cooksystem.socialmedia.repository.HashtagRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

public interface HashtagService {

    List<HashtagDto>  getAllHashtags();

    List<TweetResponseDto> getTweetsWithLabel(String formattedLabel);
}
