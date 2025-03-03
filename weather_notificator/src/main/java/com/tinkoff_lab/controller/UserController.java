package com.tinkoff_lab.controller;

import com.tinkoff_lab.dto.n.*;
import com.tinkoff_lab.external.CoordinatesDefiner;
import com.tinkoff_lab.service.n.EmailService;
import com.tinkoff_lab.service.n.UserService;
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
@RequestMapping("/user")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor

public class UserController {
    UserService service;
    CoordinatesDefiner definer;

//    @GetMapping("/get/{chatId}")
//    public ResponseEntity<List<EmailDTO>> getEmails(@PathVariable String chatId) {
//        log.debug("Received request to get all emails.");
//        var emails = service.getEmails();
//        log.info("Successfully retrieved emails.");
//        return new ResponseEntity<>(emails, HttpStatus.OK);
//    }

//    @PostMapping("/add")
//    public ResponseEntity<Void> addUser(@RequestBody UserDTO userDTO) {
//        log.debug("Received userDTO to create a user with id {}.", userDTO.chatId());
//        service.addUser(userDTO);
//        log.info("Successfully created user with id {}.", userDTO.chatId());
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable long id) {
        log.debug("Received request to get the user with id {}.", id);
        var user = service.getUser(id);
        log.info("Successfully retrieved user with id {}.", id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/getCities/{id}")
    public ResponseEntity<List<CityDTO>> getUserCities(@PathVariable long id) {
        log.debug("Received request to get the user with id {}.", id);
        var cities = service.getUserCities(id);
        log.info("Successfully retrieved user with id {}.", id);
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        log.debug("Received request to delete the user with id {}.", id);
        service.deleteUser(id);
        log.info("Successfully deleted user with id {}.", id);
        return new ResponseEntity<>("User was successfully unsubscribed.", HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<Void> updateUser(@RequestBody UserDTO userDTO) {
        log.debug("Received request to update the user with id {}.", userDTO.chatId());
        service.updateUser(userDTO);
        log.info("Successfully updated user with id {}.", userDTO.chatId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/addCities")//-------------------------------!
    public ResponseEntity<String> addCityToUser(@RequestBody UserCitiesDTO dto){
        service.addUser(dto.userDTO());

        List<CityDTO> cities = new ArrayList<>();
        for(CityDTO c: dto.cityDTOS()) {
            var crd = definer.getCoordinates(c.city(), c.country());
            cities.add(new CityDTO(c.city(), c.country(), crd.latitude(), crd.longitude()));
        }

        service.addCitiesToUser(dto.userDTO().chatId(), cities);
        return new ResponseEntity<>("Cities were added successfully to user", HttpStatus.OK);
    }

    @DeleteMapping("/deleteCities")
    public ResponseEntity<String> removeCityFromUser(@RequestBody UserCitiesDTO dto){
        service.removeCitiesFromUser(dto.userDTO().chatId(), dto.cityDTOS());
        return new ResponseEntity<>("Cities were removed successfully from user", HttpStatus.OK);
    }
}
