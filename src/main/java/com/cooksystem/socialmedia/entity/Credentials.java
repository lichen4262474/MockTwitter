package com.cooksystem.socialmedia.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Embeddable
@NoArgsConstructor
@Data
public class Credentials {
    
    @Column(unique=true)
    @NotNull(message = "username can not be null")
    private String username;
    @NotNull(message="password can not be null")
    private String password;

}
