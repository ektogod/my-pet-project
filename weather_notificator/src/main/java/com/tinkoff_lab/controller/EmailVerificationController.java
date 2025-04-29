package com.tinkoff_lab.controller;

import com.tinkoff_lab.service.n.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://26.114.102.21:8080", allowedHeaders = "*", allowCredentials = "true")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor

public class EmailVerificationController {
    EmailService service;
    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String code, @RequestParam long chatId){
        service.verifyEmail(code, chatId);
        return new ResponseEntity<>("Email was successfully verified", HttpStatusCode.valueOf(200));
    }
}
