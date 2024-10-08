package com.tinkoff_lab.controller;


import com.tinkoff_lab.dto.translation.requests.UserRequest;
import com.tinkoff_lab.dto.translation.responses.UserResponse;
import com.tinkoff_lab.service.translation.TranslationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ektogod/translateText")

public class TranslationController {                 // class for receiving requests from users
    private final TranslationServiceImpl translationService;
    private final Logger logger = LoggerFactory.getLogger(TranslationController.class);

    @Autowired
    public TranslationController(TranslationServiceImpl translationService) {
        this.translationService = translationService;
    }

    @PostMapping(consumes = "application/json",
            produces = "application/json")

    public UserResponse translate(@RequestBody UserRequest request) {
        logger.info("Request from user has received: {}", request);
        UserResponse response =  translationService.translate(request);

        logger.info("Response is sending to user");
        return response;
    }
}
