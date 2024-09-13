package com.tinkoff_lab.exception;

public class DatabaseConnectionException extends RuntimeException{  //  //exception in case of troubles with connection to db
    public DatabaseConnectionException(String message) {   // exception in case of troubles with connection to database
        super(message);
    }
}
