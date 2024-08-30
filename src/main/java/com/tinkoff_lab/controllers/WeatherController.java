package com.tinkoff_lab.controllers;

import com.tinkoff_lab.dto.weather.request.DeleteUserRequest;
import com.tinkoff_lab.dto.weather.request.WeatherRequest;
import com.tinkoff_lab.services.weather.WeatherServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ektogod/translateText")
public class WeatherController {
    private final WeatherServiceImpl weatherService;

    @Autowired
    public WeatherController(WeatherServiceImpl weatherResponse) {
        this.weatherService = weatherResponse;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<Void> subscribe(@RequestBody WeatherRequest weatherRequest){
        weatherService.add(weatherRequest);
        return new ResponseEntity<>(HttpStatusCode.valueOf(201));
    }

    @DeleteMapping("/unsubscribe")
    public ResponseEntity<Void> unsubscribe(@RequestBody DeleteUserRequest request){
        weatherService.deleteUser(request);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
}
