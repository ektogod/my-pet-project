package com.tinkoff_lab.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface WeatherClient {
    @GetExchange("/data/2.5/weather")
    String getCurrentWeatherAsString(
            @RequestParam("lat") double latitude,
            @RequestParam("lon") double longitude,
            @RequestParam("appid") String apiKey);
}
