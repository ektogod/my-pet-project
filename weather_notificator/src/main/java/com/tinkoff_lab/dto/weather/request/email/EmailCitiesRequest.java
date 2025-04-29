package com.tinkoff_lab.dto.weather.request.email;

import com.tinkoff_lab.dto.weather.CityDTOOO;

import java.util.List;

public record EmailCitiesRequest(String email, List<CityDTOOO> cities) {
}
