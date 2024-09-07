package com.tinkoff_lab.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkoff_lab.exception.WrongWeatherRequestException;
import com.tinkoff_lab.dto.Coordinates;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
//@PropertySource("classpath:application.properties")

public class CoordinatesUtils {
    static String countryCoordinatesUrl = "http://api.openweathermap.org/geo/1.0/direct?q=%s,%s&appid=01dede1cc7926f71f036644dd4350001";
    static Logger logger = LoggerFactory.getLogger(CoordinatesUtils.class);

    public static Coordinates getCoordinates(String city, String country){
        logger.info("Start coordinate definition for city {}, country {}", city, country);
        String countryCode = getCountryCode(country); //throws exception if country is wrong

        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(countryCoordinatesUrl, city, countryCode);

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

    private static String getCountryCode(String country) {
        logger.info("Start country code definition for country: {}", country);
        ObjectMapper mapper = new ObjectMapper();
        String code;
        try {
            File file = ResourceUtils.getFile("classpath:countryCodes.json");
            var map = mapper.readValue(file, Map.class);
            code = (String) map.get(country);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        if(code == null){
            logger.warn("Something wrong with country {}: code not defined.", country);
            throw new WrongWeatherRequestException("Something wrong with country: code not defined");
        }
        logger.info("Country code definition for country {} ended successfully", country);
        return code;
    }
}
