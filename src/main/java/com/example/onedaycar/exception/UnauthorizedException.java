package com.example.onedaycar.exception;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String message) {
        super(message);
    }
    public UnauthorizedException() {
        super();
    }
}
