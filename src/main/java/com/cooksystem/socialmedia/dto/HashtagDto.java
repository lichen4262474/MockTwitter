package com.cooksystem.socialmedia.dto;

import jakarta.annotation.Nonnull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@Data
public class HashtagDto {
    @Nonnull
    private String label;
    private Timestamp firstUsed;
    private Timestamp lastUsed;
}
