package com.cooksystem.socialmedia.repository;

import com.cooksystem.socialmedia.entity.Hashtag;
import com.cooksystem.socialmedia.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HashtagRepo extends JpaRepository<Hashtag,Long> {
    Optional<Hashtag> findByLabel(String formattedLabel);
}
