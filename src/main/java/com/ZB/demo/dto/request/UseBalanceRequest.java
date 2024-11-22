package com.ZB.demo.dto.request;

import com.ZB.demo.aop.AccountLockIdInterface;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UseBalanceRequest implements AccountLockIdInterface {
    private String userId;
    private String accountNumber;
    private Integer amount;
}
