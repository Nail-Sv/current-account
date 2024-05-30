package com.currentaccount.currentaccount.service;


import com.currentaccount.currentaccount.model.Account;
import com.currentaccount.currentaccount.model.Transaction;
import com.currentaccount.currentaccount.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private Account account;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void createTransaction_ShouldCreateTransaction() {

        double amount = 10.0;

        Transaction expectedTransaction = Transaction.builder()
                .account(account)
                .amount(amount)
                .created_at(LocalDateTime.now())
                .build();

        when(transactionRepository.save(any(Transaction.class))).thenReturn(expectedTransaction);

        Transaction result = transactionService.createTransaction(account, amount);

        assertNotNull(result);
        assertEquals(expectedTransaction, result);
        verify(transactionRepository).save(any(Transaction.class));
    }

}
