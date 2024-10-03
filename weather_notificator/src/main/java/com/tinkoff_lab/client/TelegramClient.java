package com.tinkoff_lab.client;

import com.tinkoff_lab.dto.weather.TelegramUserMessages;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface TelegramClient {
    @PostExchange("/telegram/currentWeather")
    void sendMessages(@RequestBody TelegramUserMessages userMessages);
}
