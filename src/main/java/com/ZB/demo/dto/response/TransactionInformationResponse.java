package com.ZB.demo.dto.response;

import com.ZB.demo.domain.TransactionResult;
import com.ZB.demo.domain.TransactionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TransactionInformationResponse {
    private String accountNumber;
    private TransactionType transactionType;
    private TransactionResult transactionResult;
    private Long transactionId;
    private Integer amount;
    private LocalDateTime transactionDateTime;
}
