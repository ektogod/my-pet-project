package com.tinkoff_lab.controllers;

import com.tinkoff_lab.dto.weather.request.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ektogod/translateText")
public class WeatherController {
    private final WeatherResponse weatherResponse;

    @Autowired
    public WeatherController(WeatherResponse weatherResponse) {
        this.weatherResponse = weatherResponse;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<Void> subscribe(@RequestBody WeatherResponse weatherResponse){
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/unsubscribe")
    public ResponseEntity<Void> unsubscribe(String email){
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
}
