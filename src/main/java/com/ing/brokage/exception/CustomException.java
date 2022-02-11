package com.ing.brokage.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Setter
@Getter
@Log4j2
public class CustomException extends RuntimeException {

    private int statusCode;

    public CustomException(final String message, int statusCode) {
        super(message);
        log.error("Custom exception occurred. message: {}, statusCode: {}", message, statusCode);
        this.setStatusCode(statusCode);
    }
}
