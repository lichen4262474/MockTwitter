package com.cooksystem.socialmedia.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
public class Hashtag {
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @Nonnull
    private String label;
    
    @CreationTimestamp
    private Timestamp firstUsed;
    
    @UpdateTimestamp
    private Timestamp lastUsed;

    @ManyToMany(mappedBy = "hashtags")
    private List<Tweet> tweets=new ArrayList<>();

    //helper method to add tweet to tweets
    public void addTweetToHashtag(Tweet tweet){
        if(!this.tweets.contains(tweet)){
            this.getTweets().add(tweet);
        }
    }
}
