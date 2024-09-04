package com.tinkoff_lab.service.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkoff_lab.exception.WrongWeatherRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class CountryCodeService {
    private final Logger logger = LoggerFactory.getLogger(CountryCodeService.class);
    public String getCountryCode(String country) {
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
            throw new WrongWeatherRequestException("Something wrong with country: code not defined.");
        }
        logger.info("Country code definition for country {} ended successfully", country);
        return code;
    }
}
