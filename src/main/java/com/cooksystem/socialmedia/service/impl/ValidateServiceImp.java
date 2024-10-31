package com.cooksystem.socialmedia.service.impl;

import com.cooksystem.socialmedia.repository.HashtagRepo;
import com.cooksystem.socialmedia.repository.UserRepo;
import com.cooksystem.socialmedia.service.interfaces.ValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateServiceImp implements ValidateService {
    private final HashtagRepo hashtagRepo;
    private final UserRepo userRepo;

    @Override
    public Boolean validateHashtagExist(String label) {
        return hashtagRepo.findByLabel(label).isPresent();
    }

    @Override
    public Boolean validateUsernameExist(String username) {
        return userRepo.findByCredentialsUsername(username).isPresent();
    }



}
