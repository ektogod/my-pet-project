package com.tinkoff_lab.exception;

public class WrongWeatherRequestException extends RuntimeException{
    public WrongWeatherRequestException(String message) {
        super(message);
    }
}
