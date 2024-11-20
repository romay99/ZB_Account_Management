package com.ZB.demo.service;

import com.ZB.demo.domain.*;
import com.ZB.demo.dto.request.CancelTransactionRequest;
import com.ZB.demo.dto.request.UseBalanceRequest;
import com.ZB.demo.dto.response.CancelTransactionResponse;
import com.ZB.demo.dto.response.UseBalanceResponse;
import com.ZB.demo.exception.*;
import com.ZB.demo.repository.AccountRepository;
import com.ZB.demo.repository.MemberRepository;
import com.ZB.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;

    /**
     * 잔액 사용하는 메서드
     */
    @Transactional
    public UseBalanceResponse useBalance(UseBalanceRequest dto) {
        String userId = dto.getUserId();
        String accountNumber = dto.getAccountNumber();
        Integer amount = dto.getAmount();

        Member member = memberRepository.findByMemberId(userId).orElseThrow(
                () -> new MemberNotFoundException("존재하지 않는 사용자 입니다.")
        );
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(
                () -> new AccountNotFoundException("존재하지 않는 계좌 입니다.")
        );
        if (!account.getMemberId().equals(userId)) {
            throw new AccountNotMatchException("계좌의 소유주가 다릅니다.");
        }
        if (account.getAccountStatus().equals(AccountStatus.UNREGISTERED)) {
            throw new AccountAlreadyUnRegisterdException("계좌가 해지 상태입니다.");
        }
        if (account.useBalance(amount)) { // 거래 성공
            accountRepository.save(account);

            Transaction transaction = Transaction.builder()
                    .transactionAccountNumber(accountNumber)
                    .result(TransactionResult.SUCCESS)
                    .transactionAmount(amount)
                    .transactionDateTime(LocalDateTime.now())
                    .build();

            Transaction savedTransaction = transactionRepository.save(transaction);

            UseBalanceResponse response = UseBalanceResponse.builder().
                    accountNumber(savedTransaction.getTransactionAccountNumber())
                    .result(savedTransaction.getResult())
                    .transactionId(savedTransaction.getId())
                    .amount(savedTransaction.getTransactionAmount())
                    .transactionDateTime(savedTransaction.getTransactionDateTime())
                    .build();

            return response;

        } else { // 거래 실패
            Transaction transaction = Transaction.builder()
                    .transactionAccountNumber(accountNumber)
                    .result(TransactionResult.FAIL)
                    .transactionAmount(amount)
                    .transactionDateTime(LocalDateTime.now())
                    .build();

            Transaction savedTransaction = transactionRepository.save(transaction);

            throw new AmountExceedBalanceException("계좌의 잔액이 충분하지 않습니다.");
        }
    }

    @Transactional
    public CancelTransactionResponse cancelTransaction(CancelTransactionRequest dto) {
        long transactionId = dto.getTransactionId();
        long amount = dto.getAmount();
        String accountNumber = dto.getAccountNumber();

        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(
                () -> new TransactionNotFoundException("존재하지 않는 거래번호입니다.")
        );
        if (transaction.getTransactionAmount() != amount) {
            throw new AmountNotMatchException("거래 금액이 일치하지 않습니다");
        }
        if (!transaction.getTransactionAccountNumber().equals(accountNumber)) {
            throw new AccountNotMatchException("계좌 번호가 일치하지 않습니다.");
        }

        if(transaction.getResult().equals(TransactionResult.SUCCESS)) { //성공했던 거래 취소하기
            Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(
                    () -> new AccountNotFoundException("존재하지 않는 계좌입니다.")
            );
            //원래 있던 계좌로 돈 복구
            account.cancelTransaction(transaction.getTransactionAmount());
            accountRepository.save(account);

            transactionRepository.delete(transaction);

            CancelTransactionResponse response = CancelTransactionResponse.builder()
                    .accountNumber(accountNumber)
                    .transactionResult(TransactionResult.SUCCESS)
                    .transactionId(transactionId)
                    .amount(amount)
                    .transactionDateTime(transaction.getTransactionDateTime())
                    .build();

            return response;

        } else { //실패했던 거래 기록 지우기
            transactionRepository.delete(transaction);
            CancelTransactionResponse response = CancelTransactionResponse.builder()
                    .accountNumber(accountNumber)
                    .transactionResult(TransactionResult.FAIL)
                    .transactionId(transactionId)
                    .amount(amount)
                    .transactionDateTime(transaction.getTransactionDateTime())
                    .build();

            return response;
        }
    }
}
