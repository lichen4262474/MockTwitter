package com.cooksystem.socialmedia.entity;

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
@NoArgsConstructor
@Data
public class Tweet {
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @ManyToOne
    @NotNull(message = "author can not be null")
    private User author;
    
    @CreationTimestamp
    private Timestamp posted;
    
    private boolean deleted;
    
    private String content;
    
    @ManyToOne
    private Tweet inReplyTo;
    
    @ManyToOne
    private Tweet repostOf;

    @OneToMany(mappedBy = "inReplyTo",fetch = FetchType.EAGER)
    private List<Tweet> replies=new ArrayList<>();

    @OneToMany(mappedBy = "repostOf")
		private List<Tweet> reposts=new ArrayList<>();

    @ManyToMany
    private List<User> usersLike=new ArrayList<>();

    @ManyToMany
    private List<User> mentionedUsers=new ArrayList<>();
    
    @ManyToMany
    private List<Hashtag> hashtags=new ArrayList<>();

    //helper method to add mentioned users:
    public void addMentionedUser(User mentionedUser) {
        if (!this.mentionedUsers.contains(mentionedUser)) {
            this.mentionedUsers.add(mentionedUser);
        }
    }
    //helper method to add hashtags
    public void addHashtags(Hashtag hashtag){
        if (!this.hashtags.contains(hashtag)) {
            this.hashtags.add(hashtag);
        }
    }
    //helper method to add user to the userLikes like
    public void addUserToLikesList(User user){
        if(!this.getUsersLike().contains(user)){
            this.getUsersLike().add(user);
        }
    }
   //helper method to add a reply to replies list

    public void addReplyToRepliesList(Tweet tweet){
        if(!this.getReplies().contains(tweet)){
            this.getReplies().add(tweet);
        }
    }
    //helper method to add a tweet to the reposts list
    public void addRepostToRepostsList(Tweet tweet){
        if(!this.getReposts().contains(tweet)){
            this.getReposts().add(tweet);
        }
    }
    //helper method to get chainOfReplies
    public List<Tweet> getChainOfReplies() {
        List<Tweet> allReplies = new ArrayList<>();
        if (!this.getReplies().isEmpty()) {
            for (Tweet reply : this.getReplies()) {
                allReplies.add(reply);    // Add the direct reply to the list
                allReplies.addAll(reply.getChainOfReplies());  // Recursively add nested replies
            }
        }
        return allReplies;
    }
    //helper method to get chain of inReplyTo tweets (recursive)
    public List<Tweet> getChainOfInReplyTo() {
        List<Tweet> chainOfInReplyTo = new ArrayList<>();

        Tweet currentTweet = this;

        // Traverse up the chain of "inReplyTo" until reaching the root tweet (a tweet not replying to any other tweet)
        while (currentTweet.getInReplyTo() != null) {
            chainOfInReplyTo.add(currentTweet.getInReplyTo());
            currentTweet = currentTweet.getInReplyTo(); // Move to the next "inReplyTo" tweet in the chain
        }

        return chainOfInReplyTo;
    }
}
