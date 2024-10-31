package com.cooksystem.socialmedia.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ProfileDto {
    private String firstName;
    private String lastName;
    @NotNull(message = "email can not be null")
    private String email;
    private String phone;
}
