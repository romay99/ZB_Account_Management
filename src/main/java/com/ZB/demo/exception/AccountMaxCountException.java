package com.ZB.demo.exception;

public class AccountMaxCountException extends RuntimeException {
    public AccountMaxCountException(String message) {
        super(message);
    }
}
