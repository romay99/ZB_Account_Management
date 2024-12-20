package com.ZB.demo.service;

import com.ZB.demo.domain.Member;
import com.ZB.demo.domain.TransactionResult;
import com.ZB.demo.domain.TransactionType;
import com.ZB.demo.dto.request.CancelTransactionRequest;
import com.ZB.demo.dto.request.CreateAccountRequest;
import com.ZB.demo.dto.request.UnRegisterAccountRequest;
import com.ZB.demo.dto.request.UseBalanceRequest;
import com.ZB.demo.dto.response.CancelTransactionResponse;
import com.ZB.demo.dto.response.CreateAccountResponse;
import com.ZB.demo.dto.response.TransactionInformationResponse;
import com.ZB.demo.dto.response.UseBalanceResponse;
import com.ZB.demo.exception.*;
import com.ZB.demo.repository.AccountRepository;
import com.ZB.demo.repository.MemberRepository;
import com.ZB.demo.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void createTestUser() {
        memberRepository.save(Member.builder()
                .memberId("test1")
                .build());
        memberRepository.save(Member.builder()
                .memberId("test2")
                .build());

    }

    @DisplayName("잔액 사용")
    @Test
    void useBalance() {
        //given
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

    @DisplayName("잔액 사용 취소")
    @Test
    public void cancelTransaction() {
        //given
        CreateAccountResponse createAccountResponse = accountService.createAccount(
                CreateAccountRequest.builder()
                        .userId("test1")
                        .initialBalance(1000)
                        .build()
        );
        UseBalanceResponse transactionResponse = transactionService.useBalance(
                UseBalanceRequest.builder()
                        .userId("test1")
                        .amount(500)
                        .accountNumber(createAccountResponse.getAccountNumber())
                        .build()
        );

        CancelTransactionRequest accountNotMatchException = CancelTransactionRequest
                .builder()
                .transactionId(transactionResponse.getTransactionId())
                .accountNumber("11111111")
                .amount(500)
                .build();

        CancelTransactionRequest transactionAmountNotMatchException = CancelTransactionRequest
                .builder()
                .transactionId(transactionResponse.getTransactionId())
                .accountNumber(transactionResponse.getAccountNumber())
                .amount(60000)
                .build();

        //when

        //then
        assertThrows(AccountNotMatchException.class, () -> transactionService.cancelTransaction(accountNotMatchException));
        assertThrows(AmountNotMatchException.class, ()-> transactionService.cancelTransaction(transactionAmountNotMatchException));

        CancelTransactionResponse cancelTransactionResponse = transactionService.cancelTransaction(
                CancelTransactionRequest.builder()
                        .transactionId(transactionResponse.getTransactionId())
                        .amount(transactionResponse.getAmount())
                        .accountNumber(transactionResponse.getAccountNumber())
                        .build());

        assertEquals(cancelTransactionResponse.getTransactionResult(), TransactionResult.SUCCESS);
        assertEquals(1000,accountRepository.findByAccountNumber(cancelTransactionResponse.getAccountNumber()).get().getBalance());
    }

    @DisplayName("거래 확인")
    @Test
    void getTransactionInformation() {
        // given
        CreateAccountResponse createAccountResponse = accountService.createAccount(
                CreateAccountRequest.builder()
                        .userId("test1")
                        .initialBalance(1000)
                        .build()
        );
        UseBalanceResponse transactionResponse = transactionService.useBalance(
                UseBalanceRequest.builder()
                        .userId("test1")
                        .amount(500)
                        .accountNumber(createAccountResponse.getAccountNumber())
                        .build()
        );

        // when
        TransactionInformationResponse response =
                transactionService.getTransactionInformation(transactionResponse.getTransactionId());

        // then
        assertEquals(response.getTransactionType(), TransactionType.USE);
        assertEquals(response.getTransactionResult(), TransactionResult.SUCCESS);

        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionInformation(99999));
    }
}