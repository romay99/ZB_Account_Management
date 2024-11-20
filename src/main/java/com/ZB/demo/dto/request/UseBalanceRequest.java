package com.ZB.demo.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UseBalanceRequest {
    private String userId;
    private String accountNumber;
    private long amount;
}
