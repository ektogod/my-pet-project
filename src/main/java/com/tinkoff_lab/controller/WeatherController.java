package com.tinkoff_lab.controller;

import com.tinkoff_lab.dto.weather.request.AddCityRequest;
import com.tinkoff_lab.dto.weather.request.EmailRequest;
import com.tinkoff_lab.dto.weather.request.WeatherRequest;
import com.tinkoff_lab.entity.CityPK;
import com.tinkoff_lab.service.weather.WeatherServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Void> unsubscribe(@RequestBody EmailRequest request){
        weatherService.deleteUser(request);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @PutMapping("/add")
    public ResponseEntity<Void> addCity(@RequestBody AddCityRequest request){
        weatherService.addCity(request);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @GetMapping("/get")
    public ResponseEntity<List<CityPK>> getCities(@RequestBody EmailRequest request){
        List<CityPK> cityPKS = weatherService.getCities(request);
        return new ResponseEntity<>(cityPKS, HttpStatusCode.valueOf(200));
    }
}
