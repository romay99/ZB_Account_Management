package com.ZB.demo.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAccountResponse {
    private String userId;
    private String accountNumber;
    private LocalDateTime madeDateTime;
}
