package com.ZB.demo.dto.request;

import com.ZB.demo.aop.AccountLockIdInterface;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CancelTransactionRequest implements AccountLockIdInterface {
    private Long transactionId;
    private String accountNumber;
    private Integer amount;
}
