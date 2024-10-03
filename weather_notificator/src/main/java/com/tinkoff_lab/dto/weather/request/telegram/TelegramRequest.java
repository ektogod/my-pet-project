package com.tinkoff_lab.dto.weather.request.telegram;

public record TelegramRequest(long chatId, String username) {
}
