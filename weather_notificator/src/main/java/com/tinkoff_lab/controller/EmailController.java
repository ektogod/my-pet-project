package com.tinkoff_lab.controller;

import com.tinkoff_lab.dto.n.EmailDTO;
import com.tinkoff_lab.service.n.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/email")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor

public class EmailController {
    EmailService service;

    @GetMapping
    public ResponseEntity<List<EmailDTO>> getEmails() {
        log.debug("Received request to get all emails.");
        var emails = service.getEmails();
        log.info("Successfully retrieved emails.");
        return new ResponseEntity<>(emails, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addEmail(@RequestBody EmailDTO request) {
        log.debug("Received request to create a room with id {}.", request.email());
        service.addEmail(request);
        log.info("Successfully created room with id {}.", request.email());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmailDTO> getEmail(@PathVariable String id) {
        log.debug("Received request to get the email with id {}.", id);
        var email = service.getEmail(id);
        log.info("Successfully retrieved email with id {}.", id);
        return new ResponseEntity<>(email, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmail(@PathVariable String id) {
        log.debug("Received request to delete the room with id {}.", id);
        service.deleteEmail(id);
        log.info("Successfully deleted room with id {}.", id);
        return new ResponseEntity<>("Email was successfully unsubscribed.", HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<Void> updateEmail(@RequestBody EmailDTO emailDTO) {
        log.debug("Received request to update the room with id {}.", emailDTO.email());
        service.updateEmail(emailDTO);
        log.info("Successfully updated room with id {}.", emailDTO.email());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
