package com.tinkoff_lab.controller;

import com.tinkoff_lab.dto.weather.request.telegram.TelegramCitiesRequest;
import com.tinkoff_lab.dto.weather.request.telegram.TelegramRequest;
import com.tinkoff_lab.dto.weather.request.telegram.WeatherTelegramRequest;
import com.tinkoff_lab.entity.CityPK;
import com.tinkoff_lab.service.weather.WeatherTelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ektogod/weather/telegram")
public class WeatherTelegramController {
    private final WeatherTelegramService weatherService;

    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribe(@RequestBody WeatherTelegramRequest request){
        weatherService.add(request);
        return new ResponseEntity<>("Subscribing ended successfully.", HttpStatusCode.valueOf(201));
    }

    @DeleteMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribe(@RequestBody TelegramRequest request){
        weatherService.deleteUser(request);
        return new ResponseEntity<>("Unsubscribing ended successfully.", HttpStatusCode.valueOf(200));
    }

    @PutMapping("/add")
    public ResponseEntity<String> addCity(@RequestBody TelegramCitiesRequest request){
        weatherService.addCity(request);
        return new ResponseEntity<>("Adding cities ended successfully.", HttpStatusCode.valueOf(200));
    }

    @GetMapping("/get")
    public ResponseEntity<List<CityPK>> getCities(@RequestBody TelegramRequest request){
        List<CityPK> cityPKS = weatherService.getCities(request);
        return new ResponseEntity<>(cityPKS, HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCities(@RequestBody TelegramCitiesRequest request){
        weatherService.deleteCities(request);
        return new ResponseEntity<>("Deleting cities ended successfully.", HttpStatusCode.valueOf(200));
    }
}

