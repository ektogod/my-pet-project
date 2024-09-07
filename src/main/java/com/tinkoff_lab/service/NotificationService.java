package com.tinkoff_lab.service;

import com.tinkoff_lab.entity.City;
import com.tinkoff_lab.entity.User;
import com.tinkoff_lab.service.database.CityDatabaseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableScheduling
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class NotificationService {
    CityDatabaseService cityDatabaseService;
    CurrentWeatherService curWeatherService;
    EmailSenderService emailSenderService;
    Logger logger = LoggerFactory.getLogger(NotificationService.class);


    //@Scheduled(fixedRate = 18000000)
    public void notifySubscribers() {
        logger.info("Start notifying all subscribers");
        List<City> cities = cityDatabaseService.findAll();
        for (City city : cities) {
            logger.info("Handling city {}", city);
            String curWeather = curWeatherService.getCurrentWeatherAsString(city);
            for (User user : city.getUsers()) {
                emailSenderService.sendEmails(user.getEmail(), curWeather);
            }
        }
        logger.info("All users were notified");
    }
}
