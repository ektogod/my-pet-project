package com.tinkoff_lab.services.weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkoff_lab.config.AppConfig;
import com.tinkoff_lab.dto.Coordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CoordinateService {
    private final AppConfig config;
    private final CountryCodeService countryCodeService;

    @Autowired
    public CoordinateService(AppConfig config, CountryCodeService countryCodeService) {
        this.config = config;
        this.countryCodeService = countryCodeService;
    }

    public Coordinates getCoordinates(String city, String country){
        String countryCode = countryCodeService.getCountryCode(country);

        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(config.getCountryCoordinatesUrl(), city, countryCode);

        String response = restTemplate.getForObject(url, String.class);
        ObjectMapper mapper = new ObjectMapper();
        try {
            var maps = mapper.readValue(response, Map[].class);
            return new Coordinates((double) maps[0].get("lat"), (double) maps[0].get("lon"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
