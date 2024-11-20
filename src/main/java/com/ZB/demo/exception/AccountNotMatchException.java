package com.ZB.demo.exception;

public class AccountNotMatchException extends RuntimeException {
    public AccountNotMatchException(String message) {
        super(message);
    }
}
