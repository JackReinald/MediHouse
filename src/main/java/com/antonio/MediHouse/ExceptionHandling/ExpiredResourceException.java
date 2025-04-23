package com.antonio.MediHouse.ExceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExpiredResourceException extends RuntimeException{
    public ExpiredResourceException(String message) {
        super(message);
    }

    public ExpiredResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
