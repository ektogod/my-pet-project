package com.tinkoff_lab.dto.weather.request.email;

import com.tinkoff_lab.dto.weather.CityDTO;
import jakarta.validation.constraints.Email;

import java.util.List;

public record WeatherEmailRequest(@Email(message = "Invalid email") String email, List<CityDTO> cities) {
}
