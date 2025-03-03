package com.tinkoff_lab.dto.n;

import java.util.List;

public record EmailCitiesDto(String email, List<CityDTO> cityDTOS) {
}
