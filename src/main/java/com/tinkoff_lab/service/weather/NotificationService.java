package com.tinkoff_lab.service.weather;

import com.tinkoff_lab.EmailSender;
import com.tinkoff_lab.client.TelegramClient;
import com.tinkoff_lab.dto.weather.TelegramUserMessages;
import com.tinkoff_lab.entity.City;
import com.tinkoff_lab.entity.TelegramUser;
import com.tinkoff_lab.entity.User;
import com.tinkoff_lab.external.CurrentWeatherDefiner;
import com.tinkoff_lab.service.database.CityDatabaseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@EnableScheduling
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class NotificationService {
    CityDatabaseService cityDatabaseService;
    CurrentWeatherDefiner curWeatherService;
    EmailSender emailSender;
    TelegramClient client;
    Logger logger = LoggerFactory.getLogger(NotificationService.class);


    @Scheduled(fixedRate = 100000)
    @Transactional
    public void notifySubscribers() {
        logger.info("Start notifying all subscribers");
        List<City> cities = cityDatabaseService.findAll();
        TelegramUserMessages userMessages = new TelegramUserMessages();
        for (City city : cities) {
            logger.info("Handling city {}", city);
            String curWeather = curWeatherService.getCurrentWeatherAsString(city);
            for (User user : city.getUsers()) {
                emailSender.sendEmails(user.getEmail(), curWeather);
            }
            for(TelegramUser user: city.getTgUsers()){
                userMessages.getChatIds().add(user.getChatId());
                String msg = String.format("Hey, %s, here is your current weather!\n\n%s", user.getFirstname(), curWeather);
                userMessages.getMessages().add(msg);
            }
        }

        client.sendMessages(userMessages);
        logger.info("All users were notified");
    }
}
