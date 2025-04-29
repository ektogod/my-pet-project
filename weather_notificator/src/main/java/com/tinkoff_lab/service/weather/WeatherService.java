package com.tinkoff_lab.service.weather;

import com.tinkoff_lab.dto.EmailUserDTO;
import com.tinkoff_lab.dto.weather.request.email.EmailCitiesRequest;
import com.tinkoff_lab.dto.weather.request.email.EmailRequest;
import com.tinkoff_lab.dto.weather.request.email.EmailCitiesDTO;
import com.tinkoff_lab.entity.CityPK;

import java.util.List;

public interface WeatherService {

    void register(EmailUserDTO request);
    void add(EmailCitiesDTO request);

    void deleteUser(EmailRequest email);

    void addCity(EmailCitiesRequest request);

    List<CityPK> getCities(EmailRequest email);

    List<String> getEmails(long chatId);

    void deleteCities(EmailCitiesRequest request);
}
