package com.runapp.storageservice.exception;

import org.springframework.stereotype.Component;

public class IoException extends RuntimeException {
    public IoException(String message) {
        super(message);
    }
}
