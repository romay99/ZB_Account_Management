package com.ZB.demo.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnRegisterAccountResponse {
    private String userId;
    private String accountNumber;
    private LocalDateTime unRegisterDateTime;
}
