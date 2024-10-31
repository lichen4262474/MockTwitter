package com.cooksystem.socialmedia.dto;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CredentialsDto {
    @NotNull(message = "username can not be null")
    private String username;
    @NotNull(message = "password can not be null")
    private String password;
}
