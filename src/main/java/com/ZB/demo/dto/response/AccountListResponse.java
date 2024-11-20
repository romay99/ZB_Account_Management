package com.ZB.demo.dto.response;

import com.ZB.demo.domain.Account;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AccountListResponse {
    private String accountNumber;
    private long balance;

    static public AccountListResponse toAccountListResponseDto(Account account) {
        AccountListResponse accountListResponse = new AccountListResponse();
        accountListResponse.setAccountNumber(account.getAccountNumber());
        accountListResponse.setBalance(account.getBalance());
        return accountListResponse;
    }
}
