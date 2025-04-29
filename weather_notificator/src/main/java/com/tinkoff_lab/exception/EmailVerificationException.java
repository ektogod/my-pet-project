package com.tinkoff_lab.exception;

public class EmailVerificationException extends RuntimeException{
    public EmailVerificationException(String message) {
        super(message);
    }
}
