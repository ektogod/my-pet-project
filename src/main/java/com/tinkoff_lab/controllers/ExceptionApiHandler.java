package com.tinkoff_lab.controllers;

import com.tinkoff_lab.dto.responses.ErrorResponse;
import com.tinkoff_lab.exception.DatabaseConnectionException;
import com.tinkoff_lab.exception.DatabaseException;
import com.tinkoff_lab.exception.TranslationException;
import com.tinkoff_lab.services.database.TranslationDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {     // global exception handler
    private final TranslationDatabaseService databaseService;

    @Autowired
    public ExceptionApiHandler(TranslationDatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @ExceptionHandler({ DatabaseException.class, DatabaseConnectionException.class})
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TranslationException.class)
    public ResponseEntity<ErrorResponse> handleException(TranslationException ex) {
        databaseService.insert(ex.getTranslation());
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getTranslation().status());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getTranslation().status()));
    }
}
