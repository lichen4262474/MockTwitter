package com.cooksystem.socialmedia.repository;

import com.cooksystem.socialmedia.entity.Tweet;
import com.cooksystem.socialmedia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepo extends JpaRepository<Tweet, Long> {
    List<Tweet> findByAuthorAndDeletedFalse(User author);

    List<Tweet> findAllByMentionedUsersContains(User user);

    List<Tweet> findByDeletedFalse();
}
