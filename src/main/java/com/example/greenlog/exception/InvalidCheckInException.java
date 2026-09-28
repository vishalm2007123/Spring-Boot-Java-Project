package com.example.greenlog.exception;

public class InvalidCheckInException extends RuntimeException {
    public InvalidCheckInException(String message) {
        super(message);
    }
}
