package com.tinkoff_lab.exception;

public class DatabaseException extends RuntimeException{  //exception in case of troubles with inserting in db
    public DatabaseException(String message) {  // exception in case of troubles with saving data in database
        super(message);
    }
}
