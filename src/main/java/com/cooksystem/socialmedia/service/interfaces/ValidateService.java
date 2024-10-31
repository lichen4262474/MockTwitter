package com.cooksystem.socialmedia.service.interfaces;

public interface ValidateService {
    Boolean validateHashtagExist(String label);

    Boolean validateUsernameExist(String username);
}
