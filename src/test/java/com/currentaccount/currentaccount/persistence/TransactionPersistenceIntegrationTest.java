package com.currentaccount.currentaccount.persistence;

import com.currentaccount.currentaccount.model.Account;
import com.currentaccount.currentaccount.model.Transaction;
import com.currentaccount.currentaccount.repository.AccountRepository;
import com.currentaccount.currentaccount.repository.TransactionRepository;
import jakarta.persistence.EntityManager;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest()
@ActiveProfiles("test")
public class TransactionPersistenceIntegrationTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @SqlGroup(value = {@Sql(scripts = {"/db/init.sql", "/db/init_transactions.sql"})})
    void createTransaction_shouldCreateTransaction() {

        UUID accountId = UUID.fromString("150e8400-e29b-41d4-a716-446655440000");

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AssertionError("Account not found"));

        double amount = 100.0;

        val actualTransaction = Transaction.builder()
                .account(account)
                .amount(amount)
                .created_at(LocalDateTime.now())
                .build();

        Transaction savedTransaction = transactionRepository.saveAndFlush(actualTransaction);

        entityManager.clear();

        Transaction retrievedTransaction = transactionRepository.findById(savedTransaction.getId())
                .orElseThrow(() -> new AssertionError("Transaction not found after save"));

        assertThat(savedTransaction).isEqualTo(retrievedTransaction);
    }

}
