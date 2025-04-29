package com.tinkoff_lab.client;

import com.tinkoff_lab.dto.SendEmailDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface EmailClient {
    @PostExchange("/telegram/currentWeather")
    void sendEmail(@RequestBody SendEmailDTO email);
}
