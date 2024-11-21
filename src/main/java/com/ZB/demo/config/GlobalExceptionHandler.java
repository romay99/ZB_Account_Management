package com.ZB.demo.config;

import com.ZB.demo.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountAlreadyUnRegisterdException.class)
    public ResponseEntity<ErrorResponse> handleAccountAlreadyUnRegisterdException(AccountAlreadyUnRegisterdException e) {
        ErrorResponse response = new ErrorResponse("ACCOUNT_ALREADY_UNREGISTERED", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountHasBalanceException.class)
    public ResponseEntity<ErrorResponse> handleAccountHashBalancedException(AccountHasBalanceException e) {
        ErrorResponse response = new ErrorResponse("ACCOUNT_HAS_BALANCE", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountMaxCountException.class)
    public ResponseEntity<ErrorResponse> handleAccountMaxCountException(AccountMaxCountException e) {
        ErrorResponse response = new ErrorResponse("ACCOUNT_MAX_COUNT", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFoundException(AccountNotFoundException e) {
        ErrorResponse response = new ErrorResponse("ACCOUNT_NOT_FOUND", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountNotMatchException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotMatchException(AccountNotMatchException e) {
        ErrorResponse response = new ErrorResponse("ACCOUNT_NOT_MATCH", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMemberNotFoundException(MemberNotFoundException e) {
        ErrorResponse response = new ErrorResponse("MEMBER_NOT_FOUND", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AmountExceedBalanceException.class)
    public ResponseEntity<ErrorResponse> handleAmountExceedBalanceException(AmountExceedBalanceException e) {
        ErrorResponse response = new ErrorResponse("AMOUNT_EXCEED_BALANCE", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTransactionNotFoundException(TransactionNotFoundException e) {
        ErrorResponse response = new ErrorResponse("TRANSACTION_NOT_FOUND", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AmountNotMatchException.class)
    public ResponseEntity<ErrorResponse> handleAmountNotMatchException(AmountNotMatchException e) {
        ErrorResponse response = new ErrorResponse("AMOUNT_NOT_MATCH", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse response = new ErrorResponse("ERROR", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
