package com.ZB.demo.exception;

public class AmountNotMatchException extends RuntimeException {
    public AmountNotMatchException(String message) {
        super(message);
    }
}
