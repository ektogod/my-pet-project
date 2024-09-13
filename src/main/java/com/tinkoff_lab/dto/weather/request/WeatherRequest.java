package com.tinkoff_lab.dto.weather.request;

import com.tinkoff_lab.dto.weather.CityDTO;
import jakarta.validation.constraints.Email;

import java.util.List;

public record WeatherRequest(@Email(message = "Invalid email") String email, String name, List<CityDTO> cities) {
}
