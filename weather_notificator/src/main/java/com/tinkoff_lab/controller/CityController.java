package com.tinkoff_lab.controller;

import com.tinkoff_lab.dto.n.CityDTO;
import com.tinkoff_lab.dto.weather.Coordinates;
import com.tinkoff_lab.external.CoordinatesDefiner;
import com.tinkoff_lab.service.n.CityService;
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
@RequestMapping("/city")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CityController {
    CityService service;
    CoordinatesDefiner definer;

    @GetMapping
    public ResponseEntity<List<CityDTO>> getCities() {
        log.debug("Received request to get all cities.");
        var cities = service.getCities();
        log.info("Successfully retrieved citied.");
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addCities(@RequestBody List<CityDTO> cityDTOS) {
        for(CityDTO cityDTO: cityDTOS) {
            log.debug("Received cityDTOS to create a city with id {}.", cityDTO.city());
            Coordinates crd = definer.getCoordinates(cityDTO.city(), cityDTO.country()); // throws exception if something incorrect
            service.addCity(new CityDTO(cityDTO.city(), cityDTO.country(), crd.latitude(), crd.longitude()));
            log.info("Successfully created city with id {}.", cityDTO.city());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{country}/{city}")
    public ResponseEntity<CityDTO> getCity(@PathVariable String city, @PathVariable String country) {
        log.debug("Received request to get the city with id {}.", city);
        var email = service.getCity(city, country);
        log.info("Successfully retrieved city with id {}.", city);
        return new ResponseEntity<>(email, HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteCity(String city, String country) {
        log.debug("Received request to delete the city with id {}.", city);
        service.deleteCity(city, country);
        log.info("Successfully deleted city with id {}.", city);
        return new ResponseEntity<>("City was successfully unsubscribed.", HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<Void> updateCity(@RequestBody CityDTO cityDTO) {
        log.debug("Received request to update the room with id {}.", cityDTO.city());
        service.updateCity(cityDTO);
        log.info("Successfully updated room with id {}.", cityDTO.city());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
