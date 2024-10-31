package com.cooksystem.socialmedia.dto;

import jakarta.annotation.Nonnull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class ContextDto {
    @Nonnull
    private TweetResponseDto target;
    @Nonnull
    private List<TweetResponseDto> before = new ArrayList<>();
    @Nonnull
    private List<TweetResponseDto> after = new ArrayList<>();
}