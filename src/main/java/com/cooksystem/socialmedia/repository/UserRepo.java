package com.cooksystem.socialmedia.repository;

import com.cooksystem.socialmedia.entity.Credentials;
import com.cooksystem.socialmedia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByCredentialsUsername(String username);

    Optional<User> getUserByCredentials(Credentials credentials);

    List<User> findByDeletedFalse();

    Optional<User> findByCredentials(Credentials credentials);
}
