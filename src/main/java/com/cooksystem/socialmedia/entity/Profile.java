package com.cooksystem.socialmedia.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@Data
public class Profile {
	
    private String firstName;
    
    private String lastName;
    
    @NotNull(message = "Email can not be null")
    private String email;
    
    private String phone;
}
