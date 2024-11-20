package com.ZB.demo.service;

import com.ZB.demo.domain.Account;
import com.ZB.demo.domain.AccountStatus;
import com.ZB.demo.domain.Member;
import com.ZB.demo.dto.request.CreateAccountRequest;
import com.ZB.demo.dto.request.UnRegisterAccountRequest;
import com.ZB.demo.dto.response.AccountListResponse;
import com.ZB.demo.dto.response.CreateAccountResponse;
import com.ZB.demo.dto.response.UnRegisterAccountResponse;
import com.ZB.demo.exception.*;
import com.ZB.demo.repository.AccountRepository;
import com.ZB.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;

    /**
     * 계좌 생성하는 메서드
     */
    public CreateAccountResponse createAccount(CreateAccountRequest dto) {
        String userId = dto.getUserId();
        long initialBalance = dto.getInitialBalance();

        Member member = memberRepository.findByMemberId(userId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 사용자입니다."));

        if (accountRepository.findAllByMemberId(userId).size() >= 10 ) {
            throw new AccountMaxCountException("User의 계좌갯수는 최대 10개 까지입니다.");
        }

        String accountNumber = "";
        while (true) {
            accountNumber = generateAccountNumber();
            if (checkAccountNumber(accountNumber)) {
                break;
            }
        }
        Account account = Account.builder()
                .accountNumber(accountNumber)
                .memberId(userId)
                .madeDateTime(LocalDateTime.now())
                .balance(initialBalance)
                .accountStatus(AccountStatus.IN_USE)
                .build();

        accountRepository.save(account);

        CreateAccountResponse response = CreateAccountResponse
                .builder()
                .accountNumber(account.getAccountNumber())
                .userId(account.getMemberId())
                .madeDateTime(account.getMadeDateTime())
                .build();

        return response;
    }

    /**
     * 계좌 번호 중복 체크 메서드
     * @param number 사용하고 싶은 계좌 번호
     * @return 사용가능 하면 true 리턴
     */
    private boolean checkAccountNumber(String number) {
        if (accountRepository.findByAccountNumber(number).isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 랜덤 계좌번호 생성 메서드
     * @return 랜덤한 10자리의 계좌번호
     */
    private String generateAccountNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 계좌 해지하는 메서드
     */
    public UnRegisterAccountResponse unRegisterAccount(UnRegisterAccountRequest dto) {
        String userId = dto.getUserId();
        String accountNumber = dto.getAccountNumber();

        Member member = memberRepository.findByMemberId(userId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 사용자입니다."));
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("존재하지 않는 계좌입니다."));

        if (!account.getMemberId().equals(userId)) {
            throw new AccountNotMatchException("계좌의 소유주가 다릅니다.");
        }
        if (account.getAccountStatus().equals(AccountStatus.UNREGISTERED)) {
            throw new AccountAlreadyUnRegisterdException("이미 해지된 계좌입니다.");
        }
        if (account.getBalance() > 0) {
            throw new AccountHasBalanceException("잔액이 남아있는 계좌는 해지할 수 없습니다.");
        }
        account.setAccountStatus(AccountStatus.UNREGISTERED);
        accountRepository.save(account);

        UnRegisterAccountResponse response = UnRegisterAccountResponse.builder()
                .accountNumber(accountNumber)
                .userId(userId)
                .unRegisterDateTime(LocalDateTime.now())
                .build();

        return response;
    }

    /**
     * User 의 계좌 리스트를 리턴하는 메서드
     * @param memberId User ID
     */
    public List<AccountListResponse> getAccountListByMemberId(String memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 사용자입니다."));

        List<Account> accountList = accountRepository.findAllByMemberId(memberId);
        List<AccountListResponse> list = accountList.stream().map(account ->
                        AccountListResponse.toAccountListResponseDto(account))
                .toList();

        return list;
    }
}