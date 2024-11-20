package com.ZB.demo.dto.response;

import com.ZB.demo.domain.TransactionResult;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CancelTransactionResponse {
    private String accountNumber;
    private TransactionResult transactionResult;
    private Long transactionId;
    private Long amount;
    private LocalDateTime transactionDateTime;

}
