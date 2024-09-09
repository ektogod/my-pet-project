package com.tinkoff_lab.client;

import com.tinkoff_lab.dto.weather.Coordinates;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface CoordinateClient {
    @GetExchange("/geo/1.0/direct")
    List<Coordinates> getCoordinates(@RequestParam("q") String query, @RequestParam("appid") String apiKey);
}
