package com.tinkoff_lab.dto.weather.request.telegram;

import com.tinkoff_lab.dto.weather.CityDTO;

import java.util.List;

public record TelegramCitiesRequest(long chatId, String username, List<CityDTO> cities) {
}
