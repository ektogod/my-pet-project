package com.tinkoff_lab.dto.weather.request.telegram;

import com.tinkoff_lab.dto.weather.CityDTO;

import java.util.List;

public record WeatherTelegramRequest(long chatId, String username, String firstname, String lastname, List<CityDTO> cities) {
}
