package com.ZB.demo.controller;

import com.ZB.demo.dto.request.CreateAccountRequest;
import com.ZB.demo.dto.request.UnRegisterAccountRequest;
import com.ZB.demo.dto.response.AccountListResponse;
import com.ZB.demo.dto.response.CreateAccountResponse;
import com.ZB.demo.dto.response.UnRegisterAccountResponse;
import com.ZB.demo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create-account")
    public ResponseEntity<CreateAccountResponse> createAccount(
            @RequestBody CreateAccountRequest dto) {
        return ResponseEntity.ok().body(accountService.createAccount(dto));
    }

    @PostMapping("/remove-account")
    public ResponseEntity<UnRegisterAccountResponse> deleteAccount(@RequestBody UnRegisterAccountRequest dto) {
        return ResponseEntity.ok(accountService.unRegisterAccount(dto));
    }

    @GetMapping("/account/{memberId}")
    public ResponseEntity<List<AccountListResponse>> getAccount(
            @PathVariable String memberId){
        return ResponseEntity.ok(accountService.getAccountListByMemberId(memberId));
    }
}
