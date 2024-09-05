package com.tinkoff_lab.service.weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkoff_lab.config.AppConfig;
import com.tinkoff_lab.dto.Coordinates;
import com.tinkoff_lab.exception.WrongWeatherRequestException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CoordinateService {
    private final AppConfig config;
    private final CountryCodeService countryCodeService;
    private final Logger logger = LoggerFactory.getLogger(CoordinateService.class);

    @Autowired
    public CoordinateService(AppConfig config, CountryCodeService countryCodeService) {
        this.config = config;
        this.countryCodeService = countryCodeService;
    }

    public Coordinates getCoordinates(String city, String country){
        logger.info("Start coordinate definition for city {}, country {}", city, country);
        String countryCode = countryCodeService.getCountryCode(country); //throws exception if country is wrong

        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(config.getCountryCoordinatesUrl(), city, countryCode);

        String response = restTemplate.getForObject(url, String.class);
        if (response != null && response.equals("[]")){
            logger.warn("Something wrong with location. Coordinates not defined for city {}, country {}", city, country);
            throw new WrongWeatherRequestException("Something wrong with location. Coordinates not defined");
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            var maps = mapper.readValue(response, Map[].class);
            logger.info("Coordinate definition for city {}, country {} ended successfully", city, country);
            return new Coordinates((double) maps[0].get("lat"), (double) maps[0].get("lon"));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
