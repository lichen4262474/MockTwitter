package com.cooksystem.socialmedia.controller;

import com.cooksystem.socialmedia.dto.HashtagDto;
import com.cooksystem.socialmedia.service.interfaces.HashtagService;
import com.cooksystem.socialmedia.service.interfaces.ValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/validate")
public class ValidateController {
    private final ValidateService validateService;
    @GetMapping({"/tag/exists/{label}"})
    public Boolean validateHashtagExist(@PathVariable String label){
        return validateService.validateHashtagExist(label);
    }

    @GetMapping("/username/exists/@{username}")
    public Boolean validateUsernameExist(@PathVariable String username){
        return validateService.validateUsernameExist(username);
    }
    @GetMapping("/username/available/@{username}")
    public Boolean validateUsernameAvailable(@PathVariable String username){
        return !validateService.validateUsernameExist(username);
    }

}
