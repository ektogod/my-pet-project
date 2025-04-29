package com.tinkoff_lab.controller;

import com.tinkoff_lab.dto.n.CityDTO;
import com.tinkoff_lab.dto.n.EmailCitiesDto;
import com.tinkoff_lab.dto.n.EmailDTO;
import com.tinkoff_lab.external.CoordinatesDefiner;
import com.tinkoff_lab.service.n.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/email")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor

public class EmailController {
    EmailService service;
    CoordinatesDefiner definer;

    @GetMapping("/get/{chatId}")
    public ResponseEntity<List<EmailDTO>> getEmails(@PathVariable String chatId) {
        log.debug("Received request to get all emails.");
        var emails = service.getEmails();
        log.info("Successfully retrieved emails.");
        return new ResponseEntity<>(emails, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addEmail(@RequestBody EmailDTO request) {
        log.debug("Received request to create a email with id {}.", request.email());
        service.addEmail(request);
        log.info("Successfully created email with id {}.", request.email());
        return new ResponseEntity<>(" ", HttpStatus.OK);
    }

    @GetMapping("/{email}/getCities")
    public ResponseEntity<List<CityDTO>> getEmailCities(@PathVariable String email, @RequestParam long chatId){
        log.debug("Received request to get cities from email {}.", email);
        var cities = service.getEmailCities(email, chatId);
        log.info("Successfully created email with id {}.", email);
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }

    @GetMapping("/{id}/get")
    public ResponseEntity<EmailDTO> getEmail(@PathVariable String id) {
        log.debug("Received request to get the email with id {}.", id);
        var email = service.getEmail(id);
        log.info("Successfully retrieved email with id {}.", id);
        return new ResponseEntity<>(email, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteEmail(@PathVariable String id, @RequestParam(name = "chatId") long chatId) {
        log.debug("Received request to delete the room with id {}.", id);
        service.deleteEmail(id, chatId);
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

    @PostMapping("/{email}/addCities")
    public ResponseEntity<String> addCityToEmail(@PathVariable String email, @RequestBody List<CityDTO> cityDTOS, @RequestParam long chatId){
        List<CityDTO> cities = new ArrayList<>();
        for(CityDTO c: cityDTOS) {
            var crd = definer.getCoordinates(c.city(), c.country());
            cities.add(new CityDTO(c.city(), c.country(), crd.latitude(), crd.longitude()));
        }

        service.addCitiesToEmail(email,chatId, cities);
        return new ResponseEntity<>("Cities was added successfully to email", HttpStatus.OK);
    }

    @DeleteMapping("/{email}/deleteCities")
    public ResponseEntity<String> removeCityFromEmail(@PathVariable String email, @RequestBody List<CityDTO> cityDTOS, @RequestParam long chatId){
        service.removeCitiesFromEmail(email, chatId, cityDTOS);
        return new ResponseEntity<>("Cities was removed successfully to email", HttpStatus.OK);
    }
}
