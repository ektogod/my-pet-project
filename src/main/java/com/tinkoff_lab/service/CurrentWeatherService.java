package com.tinkoff_lab.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkoff_lab.CurrentWeather;
import com.tinkoff_lab.config.AppConfig;
import com.tinkoff_lab.entity.City;
import com.tinkoff_lab.entity.CityPK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrentWeatherService {
    private final AppConfig appConfig;
    private final Logger logger = LoggerFactory.getLogger(CurrentWeatherService.class);

    @Autowired
    public CurrentWeatherService(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public String getCurrentWeatherAsString(City city) {
        logger.info("Start getting current weather for city {}", city);
        String url = String.format(appConfig.getCurrentWeatherUrl(), city.getLatitude(), city.getLongitude());

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        logger.info("Response from Weather API received");

        return parseResponse(response, city.getPk()).toString();
    }

    private CurrentWeather parseResponse(String response, CityPK pk) {
        ObjectMapper mapper = new ObjectMapper();
        logger.info("Start parsing Weather API response");
        try {
            JsonNode rootNode = mapper.readTree(response);
            JsonNode weatherNode = rootNode.get("weather");
            JsonNode mainNode = rootNode.get("main");
            JsonNode windNode = rootNode.get("wind");

            String weather = weatherNode.get(0).get("main").toString();
            String weatherDescription = weatherNode.get(0).get("description").toString();
            double curTemp = mainNode.get("temp").asDouble() - 273.15;
            double feelsLike = mainNode.get("feels_like").asDouble() - 273.15;
            double pressure = mainNode.get("pressure").asDouble();
            double humidity = mainNode.get("humidity").asDouble();
            double windSpeed = windNode.get("speed").asDouble();
            logger.info("Parsing ended successfully");
            return new CurrentWeather(pk.getCity(),
                    pk.getCountry(),
                    weather,
                    weatherDescription,
                    curTemp,
                    feelsLike,
                    pressure,
                    humidity,
                    windSpeed);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
