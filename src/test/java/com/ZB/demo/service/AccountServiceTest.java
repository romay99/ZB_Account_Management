package com.ZB.demo.service;

import com.ZB.demo.domain.Member;
import com.ZB.demo.dto.request.CreateAccountRequest;
import com.ZB.demo.dto.request.UnRegisterAccountRequest;
import com.ZB.demo.dto.response.AccountListResponse;
import com.ZB.demo.dto.response.CreateAccountResponse;
import com.ZB.demo.dto.response.UnRegisterAccountResponse;
import com.ZB.demo.exception.*;
import com.ZB.demo.repository.AccountRepository;
import com.ZB.demo.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AccountServiceTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountService accountService;

    @BeforeEach
    void createTestUser() {
        // test1 , test2 두명의 유저 생성
        memberRepository.save(Member.builder()
                .memberId("test1")
                .build());
        memberRepository.save(Member.builder()
                .memberId("test2")
                .build());
    }

    @Test
    void createAccount() {
        //given
        CreateAccountRequest request1 = CreateAccountRequest.builder()
                .initialBalance(100)
                .userId("test1")
                .build();

        CreateAccountRequest memberNotFoundRequest = CreateAccountRequest.builder()
                .initialBalance(100)
                .userId("empty")
                .build();

        //when
        CreateAccountResponse response = accountService.createAccount(request1);
        for (int i = 0; i < 9; i++) {
            accountService.createAccount(request1);
        }

        //then
        // 존재하지 않는 유저ID 예외 검증
        assertThrows(MemberNotFoundException.class, () -> accountService.createAccount(memberNotFoundRequest));
        //계좌 최대치는 10개 예외 검증
        assertThrows(AccountMaxCountException.class, ()-> accountService.createAccount(request1));
        assertEquals(accountRepository.findAllByMemberId("test1").size(),10);
    }

    @Test
    void unRegisterAccount() {
        //given
        CreateAccountRequest createBalance1 = CreateAccountRequest.builder()
                .initialBalance(1000)
                .userId("test1")
                .build();

        CreateAccountRequest createBalance2 = CreateAccountRequest.builder()
                .initialBalance(0)
                .userId("test1")
                .build();

        CreateAccountResponse balanceResponse = accountService.createAccount(createBalance1);
        CreateAccountResponse noBalanceResponse = accountService.createAccount(createBalance2);

        UnRegisterAccountRequest userNotFoundException = UnRegisterAccountRequest.builder()
                .userId("empty")
                .accountNumber(balanceResponse.getAccountNumber())
                .build();

        UnRegisterAccountRequest userNotMatchException = UnRegisterAccountRequest.builder()
                .userId("test2")
                .accountNumber(balanceResponse.getAccountNumber())
                .build();

        UnRegisterAccountRequest hasBalanceException = UnRegisterAccountRequest.builder()
                .userId("test1")
                .accountNumber(balanceResponse.getAccountNumber())
                .build();

        UnRegisterAccountRequest noBalanceRequest = UnRegisterAccountRequest.builder()
                .userId("test1")
                .accountNumber(noBalanceResponse.getAccountNumber())
                .build();

        //when
        //정상 수행 코드
        UnRegisterAccountResponse response = accountService.unRegisterAccount(noBalanceRequest);

        //then
        assertThrows(MemberNotFoundException.class,()-> accountService.unRegisterAccount(userNotFoundException));
        assertThrows(AccountNotMatchException.class,()-> accountService.unRegisterAccount(userNotMatchException));
        assertThrows(AccountHasBalanceException.class,()-> accountService.unRegisterAccount(hasBalanceException));
        assertThrows(AccountAlreadyUnRegisterdException.class, () -> accountService.unRegisterAccount(noBalanceRequest));
        assertNotNull(response.getUnRegisterDateTime());
    }

    @Test
    void getAccountListByMemberId() {
        //given
        String memberId = "test1";
        String userNotFoundException = "empty";

        CreateAccountRequest request = CreateAccountRequest.builder()
                .userId("test1")
                .initialBalance(100)
                .build();

        //when
        accountService.createAccount(request);
        accountService.createAccount(request);
        accountService.createAccount(request);

        List<AccountListResponse> accountList = accountService.getAccountListByMemberId(memberId);

        //then
        assertNotEquals( 0,accountList.size());
        assertEquals(3,accountList.size());
        assertThrows(MemberNotFoundException.class, () -> accountService.getAccountListByMemberId(userNotFoundException));
    }
}