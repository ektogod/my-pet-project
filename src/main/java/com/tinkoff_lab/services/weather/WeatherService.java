package com.tinkoff_lab.services.weather;

import com.tinkoff_lab.dto.weather.request.DeleteUserRequest;
import com.tinkoff_lab.dto.weather.request.WeatherRequest;

public interface WeatherService {
    void add(WeatherRequest request);

    void deleteUser(DeleteUserRequest request);
}
