package com.tinkoff_lab.controller;

import com.tinkoff_lab.dto.weather.request.AddCityRequest;
import com.tinkoff_lab.dto.weather.request.DeleteUserRequest;
import com.tinkoff_lab.dto.weather.request.WeatherRequest;
import com.tinkoff_lab.service.weather.WeatherServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ektogod/weather")
public class WeatherController {
    private final WeatherServiceImpl weatherService;

    @Autowired
    public WeatherController(WeatherServiceImpl weatherResponse) {
        this.weatherService = weatherResponse;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<Void> subscribe(@Valid @RequestBody WeatherRequest weatherRequest){
        weatherService.add(weatherRequest);
        return new ResponseEntity<>(HttpStatusCode.valueOf(201));
    }

    @DeleteMapping("/unsubscribe")
    public ResponseEntity<Void> unsubscribe(@RequestBody DeleteUserRequest request){
        weatherService.deleteUser(request);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @PutMapping("/add")
    public ResponseEntity<Void> addCity(@RequestBody AddCityRequest request){
        weatherService.addCity(request);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
}
