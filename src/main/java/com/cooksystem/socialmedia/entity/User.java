package com.cooksystem.socialmedia.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name="user_table")
public class User {
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @CreationTimestamp
    private Timestamp joined;
    
    private boolean deleted;
    
    @Embedded
    @NotNull(message = "Credentials can not be null")
    private Credentials credentials;

    @Embedded
    @NotNull(message = "Profile can not be null")
    private Profile profile;
    
    @ManyToMany
    private List<User> followers=new ArrayList<>();
    
    @ManyToMany(mappedBy = "followers")
    private List<User> followings=new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.PERSIST)
    private List<Tweet> tweets=new ArrayList<>();
    
    @ManyToMany(mappedBy = "usersLike")
    private List<Tweet> likedTweets=new ArrayList<>();

    @ManyToMany(mappedBy="mentionedUsers")
    private List<Tweet> mentionedTweets=new ArrayList<>();

    //helper method to add follower
    public void addFollower(User follower) {
        if (!this.followers.contains(follower)) {
            this.followers.add(follower);
        }
    }
    //helper method to follow another user
    public void follow(User userToFollow) {
        if (!this.followings.contains(userToFollow)) {
            this.followings.add(userToFollow);
        }
    }
    //helper method to delete follower
    public void deleteFollower(User follower) {
        if (this.followers.contains(follower)) {
            this.followers.remove(follower);
        }
    }
    //helper method to unfollow a user
    public void unfollow(User userToUnfollow) {
        if (this.followings.contains(userToUnfollow)) {
            this.followings.remove(userToUnfollow);
        }
    }
    //helper method to add mentioned tweet
    public void addMentionedTweet(Tweet tweet){
        if(!this.getMentionedTweets().contains(tweet)){
            this.getMentionedTweets().add(tweet);
        }
    }
    //helper method to add tweet to the likedTweet list
    public void addLikedTweet(Tweet tweet){
        if(!this.getLikedTweets().contains(tweet)){
            this.getLikedTweets().add(tweet);
        }
    }
    //helper method to add tweet to tweets list
    public void addTweet(Tweet tweet){
        if(!this.getTweets().contains(tweet)){
            this.getTweets().add(tweet);
        }
    }

}
