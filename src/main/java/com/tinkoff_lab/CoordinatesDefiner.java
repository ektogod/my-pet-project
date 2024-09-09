package com.tinkoff_lab;

import com.tinkoff_lab.client.CoordinateClient;
import com.tinkoff_lab.config.AppConfig;
import com.tinkoff_lab.dto.weather.Coordinates;
import com.tinkoff_lab.exception.WrongWeatherRequestException;
import com.tinkoff_lab.utils.CountryCodeUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component

public class CoordinatesDefiner {
    Logger logger = LoggerFactory.getLogger(CoordinatesDefiner.class);
    CoordinateClient client;
    AppConfig config;
    public Coordinates getCoordinates(String city, String country) {
        logger.info("Start coordinate definition for city {}, country {}", city, country);
        String countryCode = CountryCodeUtils.getCountryCode(country); //throws exception if country is wrong

        List<Coordinates> response = client.getCoordinates(String.format("%s,%s", city, countryCode), config.getOpenWeatherKey());
        if (response.isEmpty()) {
            logger.warn("Something wrong with location. Coordinates not defined for city {}, country {}", city, country);
            throw new WrongWeatherRequestException("Something wrong with location. Coordinates not defined");
        }

        logger.info("Coordinate definition for city {}, country {} ended successfully", city, country);
        return response.get(0);
    }
}
