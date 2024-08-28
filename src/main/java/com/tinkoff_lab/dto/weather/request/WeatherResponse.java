package com.tinkoff_lab.dto.weather.request;

import java.util.List;

public record WeatherResponse(String email, List<String> cities) {
}
