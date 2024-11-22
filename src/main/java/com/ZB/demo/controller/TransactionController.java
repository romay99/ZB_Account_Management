package com.ZB.demo.controller;

import com.ZB.demo.aop.AccountLock;
import com.ZB.demo.dto.request.CancelTransactionRequest;
import com.ZB.demo.dto.request.UseBalanceRequest;
import com.ZB.demo.dto.response.CancelTransactionResponse;
import com.ZB.demo.dto.response.TransactionInformationResponse;
import com.ZB.demo.dto.response.UseBalanceResponse;
import com.ZB.demo.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/use-balance")
    @AccountLock
    public ResponseEntity<UseBalanceResponse> useBalance(@RequestBody UseBalanceRequest dto) {
        return ResponseEntity.ok().body(transactionService.useBalance(dto));
    }

    @DeleteMapping("/transaction")
    @AccountLock
    public ResponseEntity<CancelTransactionResponse> cancelTransaction(@RequestBody CancelTransactionRequest dto) {
        return ResponseEntity.ok().body(transactionService.cancelTransaction(dto));
    }

    @GetMapping("/transaction/{id}")
    public ResponseEntity<TransactionInformationResponse> getTransactionInformation(@PathVariable long id) {
        return ResponseEntity.ok().body(transactionService.getTransactionInformation(id));
    }
}
