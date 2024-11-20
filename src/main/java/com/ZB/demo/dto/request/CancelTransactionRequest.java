package com.ZB.demo.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CancelTransactionRequest {
    private Long transactionId;
    private String accountNumber;
    private Integer amount;
}
