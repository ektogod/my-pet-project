package com.tinkoff_lab.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkoff_lab.client.WeatherClient;
import com.tinkoff_lab.config.AppConfig;
import com.tinkoff_lab.dto.weather.CurrentWeather;
import com.tinkoff_lab.entity.City;
import com.tinkoff_lab.entity.CityPK;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)

public class CurrentWeatherDefiner {
    AppConfig appConfig;
    Logger logger = LoggerFactory.getLogger(CurrentWeatherDefiner.class);
    WeatherClient client;

    public String getCurrentWeatherAsString(City city) {
        logger.info("Start getting current weather for city {}", city);
        String response = client.getCurrentWeatherAsString(city.getLatitude(), city.getLongitude(), appConfig.getOpenWeatherKey());
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
