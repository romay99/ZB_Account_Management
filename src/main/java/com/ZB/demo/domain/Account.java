package com.ZB.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Account {
    @Id
    @GeneratedValue
    private Long id;

    private String accountNumber;

    private String memberId;

    private Integer balance;

    private LocalDateTime madeDateTime;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    public boolean useBalance(Integer amount) {
        if (this.balance >= amount) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    public void cancelTransaction(Integer amount) {
        this.balance += amount;
    }
}
