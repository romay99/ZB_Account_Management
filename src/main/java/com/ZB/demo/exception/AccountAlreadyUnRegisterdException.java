package com.ZB.demo.exception;

public class AccountAlreadyUnRegisterdException extends RuntimeException{
    public AccountAlreadyUnRegisterdException(String message) {
        super(message);
    }
}
