package com.ZB.demo.service;

import com.ZB.demo.domain.Member;
import com.ZB.demo.domain.TransactionResult;
import com.ZB.demo.dto.request.CreateAccountRequest;
import com.ZB.demo.dto.request.UnRegisterAccountRequest;
import com.ZB.demo.dto.request.UseBalanceRequest;
import com.ZB.demo.dto.response.CreateAccountResponse;
import com.ZB.demo.dto.response.UseBalanceResponse;
import com.ZB.demo.exception.AccountAlreadyUnRegisterdException;
import com.ZB.demo.exception.AccountNotMatchException;
import com.ZB.demo.exception.AmountExceedBalanceException;
import com.ZB.demo.exception.MemberNotFoundException;
import com.ZB.demo.repository.MemberRepository;
import com.ZB.demo.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TransactionServiceTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TransactionService transactionService;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountService accountService;

    @BeforeEach
    void createTestUser() {
        memberRepository.save(Member.builder()
                .memberId("test1")
                .build());
        memberRepository.save(Member.builder()
                .memberId("test2")
                .build());

    }

    @Test
    void useBalance() {
        //give
        CreateAccountRequest hasBalanceAccount = CreateAccountRequest.builder()
                .userId("test1")
                .initialBalance(1000)
                .build();

        CreateAccountRequest zeroBalanceAccount = CreateAccountRequest.builder()
                .userId("test1")
                .initialBalance(0)
                .build();

        CreateAccountResponse createAccountResponse = accountService.createAccount(hasBalanceAccount);
        CreateAccountResponse unUseAccount = accountService.createAccount(zeroBalanceAccount);

        accountService.unRegisterAccount(UnRegisterAccountRequest.builder()
                .userId("test1")
                .accountNumber(unUseAccount.getAccountNumber())
                .build());

        UseBalanceRequest request = UseBalanceRequest.builder()
                .userId("test1")
                .accountNumber(createAccountResponse.getAccountNumber())
                .amount(500)
                .build();

        UseBalanceRequest userNotFoundException = UseBalanceRequest.builder()
                .userId("empty")
                .accountNumber(createAccountResponse.getAccountNumber())
                .amount(500)
                .build();

        UseBalanceRequest userNotMatchException = UseBalanceRequest.builder()
                .userId("test2")
                .accountNumber(createAccountResponse.getAccountNumber())
                .amount(500)
                .build();

        UseBalanceRequest accountAlreadyUnRegisterException = UseBalanceRequest.builder()
                .userId("test1")
                .accountNumber(unUseAccount.getAccountNumber())
                .amount(500)
                .build();

        UseBalanceRequest amountExceedBalanceException = UseBalanceRequest.builder()
                .userId("test1")
                .accountNumber(createAccountResponse.getAccountNumber())
                .amount(100000)
                .build();

        //when
        //정상거래. 계좌의 남은돈 500
        UseBalanceResponse response = transactionService.useBalance(request);

        //then
        assertThrows(MemberNotFoundException.class, () -> transactionService.useBalance(userNotFoundException));
        assertThrows(AccountNotMatchException.class, () -> transactionService.useBalance(userNotMatchException));
        assertThrows(AccountAlreadyUnRegisterdException.class, () -> transactionService.useBalance(accountAlreadyUnRegisterException));
        assertThrows(AmountExceedBalanceException.class, () -> transactionService.useBalance(amountExceedBalanceException));
        assertEquals(500,response.getAmount());
        assertEquals(TransactionResult.SUCCESS, response.getResult());
    }
}