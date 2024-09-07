package com.tinkoff_lab.service.weather;

import com.tinkoff_lab.dto.weather.request.EmailCitiesRequest;
import com.tinkoff_lab.dto.weather.request.EmailRequest;
import com.tinkoff_lab.dto.weather.request.WeatherRequest;
import com.tinkoff_lab.entity.CityPK;

import java.util.List;

public interface WeatherService {
    void add(WeatherRequest request);

    void deleteUser(EmailRequest email);

    void addCity(EmailCitiesRequest request);

    List<CityPK> getCities(EmailRequest email);

    void deleteCities(EmailCitiesRequest request);
}
