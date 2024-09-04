package com.tinkoff_lab.dto.weather.request;

import com.tinkoff_lab.dto.CityDTO;

import java.util.List;

public record AddCityRequest(String email, List<CityDTO> cities) {
}
