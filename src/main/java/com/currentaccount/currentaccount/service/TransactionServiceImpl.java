package com.currentaccount.currentaccount.service;

import com.currentaccount.currentaccount.model.Account;
import com.currentaccount.currentaccount.model.Transaction;
import com.currentaccount.currentaccount.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public Transaction createTransaction(Account account, double amount) {

        log.info("Creating Transaction for Account {}", account);

        val transaction = Transaction.builder()
                .account(account)
                .amount(amount)
                .created_at(LocalDateTime.now())
                .build();

        return transactionRepository.save(transaction);
    }
}
