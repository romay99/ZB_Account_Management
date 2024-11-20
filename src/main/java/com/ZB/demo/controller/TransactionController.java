package com.ZB.demo.controller;

import com.ZB.demo.dto.request.UseBalanceRequest;
import com.ZB.demo.dto.response.UseBalanceResponse;
import com.ZB.demo.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/use-balance")
    public ResponseEntity<UseBalanceResponse> useBalance(@RequestBody UseBalanceRequest dto) {
        return ResponseEntity.ok().body(transactionService.useBalance(dto));
    }

}
