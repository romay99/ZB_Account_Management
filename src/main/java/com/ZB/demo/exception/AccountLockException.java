package com.ZB.demo.exception;

public class AccountLockException extends RuntimeException{
    public AccountLockException(String message) {
        super(message);
    }
}
