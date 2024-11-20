package com.ZB.demo.exception;

public class AmountExceedBalanceException extends RuntimeException {
    public AmountExceedBalanceException(String message) {
        super(message);
    }
}
