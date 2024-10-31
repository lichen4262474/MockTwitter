package com.cooksystem.socialmedia.dto;

import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TweetRequestDto {
    @NotNull(message = "content can not be null")
    @Valid
    private String content;
    @NotNull(message="credentials can not be null")
    @Valid
    private CredentialsDto credentials;
}
