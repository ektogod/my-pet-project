package com.tinkoff_lab.service.weather;

import com.tinkoff_lab.dto.weather.request.AddCityRequest;
import com.tinkoff_lab.dto.weather.request.DeleteUserRequest;
import com.tinkoff_lab.dto.weather.request.WeatherRequest;

public interface WeatherService {
    void add(WeatherRequest request);

    void deleteUser(DeleteUserRequest request);

    void addCity(AddCityRequest request);
}
