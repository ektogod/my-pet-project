package com.tinkoff_lab.service;

import com.tinkoff_lab.entity.City;
import com.tinkoff_lab.entity.User;
import com.tinkoff_lab.service.database.CityDatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableScheduling
public class NotificationService {
    private final CityDatabaseService cityDatabaseService;
    private final CurrentWeatherService curWeatherService;
    private final EmailSenderService emailSenderService;
    private final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    public NotificationService(CityDatabaseService cityDatabaseService, CurrentWeatherService curWeatherService, EmailSenderService emailSenderService) {
        this.cityDatabaseService = cityDatabaseService;
        this.curWeatherService = curWeatherService;
        this.emailSenderService = emailSenderService;
    }

    //@Scheduled(fixedRate = 18000000)
    public void notifySubscribers(){
        logger.info("Start notifying all subscribers");
        List<City> cities = cityDatabaseService.findAll();
        for(City city: cities){
            logger.info("Handling city {}", city);
            String curWeather = curWeatherService.getCurrentWeatherAsString(city);
            for(User user: city.getUsers()) {
                emailSenderService.sendEmails(user.getEmail(), curWeather);
            }
        }
        logger.info("All users were notified");
    }
}
