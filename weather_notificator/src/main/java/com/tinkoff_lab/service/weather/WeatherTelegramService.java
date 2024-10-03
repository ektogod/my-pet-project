package com.tinkoff_lab.service.weather;

import com.tinkoff_lab.dto.weather.request.telegram.TelegramCitiesRequest;
import com.tinkoff_lab.dto.weather.request.telegram.TelegramRequest;
import com.tinkoff_lab.dto.weather.request.telegram.WeatherTelegramRequest;
import com.tinkoff_lab.entity.CityPK;

import java.util.List;

public interface WeatherTelegramService {
    void add(WeatherTelegramRequest request);

    void deleteUser(TelegramRequest email);

    List<CityPK> getCities(TelegramRequest email);

    void deleteCities(TelegramCitiesRequest request);
}
