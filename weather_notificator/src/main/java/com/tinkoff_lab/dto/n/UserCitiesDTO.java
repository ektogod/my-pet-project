package com.tinkoff_lab.dto.n;

import java.util.List;

public record UserCitiesDTO(UserDTO userDTO, List<CityDTO> cityDTOS) {
}
