package com.tinkoff_lab.service.weather;

import com.tinkoff_lab.dto.weather.request.AddCityRequest;
import com.tinkoff_lab.dto.weather.request.EmailRequest;
import com.tinkoff_lab.dto.weather.request.WeatherRequest;
import com.tinkoff_lab.entity.CityPK;

import java.util.List;

public interface WeatherService {
    void add(WeatherRequest request);

    void deleteUser(EmailRequest email);

    void addCity(AddCityRequest request);

    List<CityPK> getCities(EmailRequest email);
}
