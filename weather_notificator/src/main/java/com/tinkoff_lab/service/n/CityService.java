package com.tinkoff_lab.service.n;

import com.tinkoff_lab.dto.n.EmailDTO;
import com.tinkoff_lab.dto.weather.CityDTO;

import java.util.List;

public interface CityService {
    List<CityDTO> getCities();

    EmailDTO getCity(String city, String country);

    void deleteCity(String city, String country);

    CityDTO updateCity(CityDTO cityDTO);

    CityDTO addCity(CityDTO cityDTO);
}
