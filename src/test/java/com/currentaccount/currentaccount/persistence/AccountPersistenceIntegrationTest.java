package com.currentaccount.currentaccount.persistence;

import com.currentaccount.currentaccount.model.Account;
import com.currentaccount.currentaccount.model.Customer;
import com.currentaccount.currentaccount.repository.AccountRepository;
import jakarta.persistence.EntityManager;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest()
@ActiveProfiles("test")
public class AccountPersistenceIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @SqlGroup(value = {@Sql(scripts = {"/db/init.sql"})})
    void createAccount_shouldCreateAccount() {

        val customer = Customer.builder()
                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))
                .name("John")
                .surname("Doe")
                .balance(10)
                .build();

        val actualAccount = Account.builder()
                .customer(customer)
                .balance(10)
                .build();

        accountRepository.saveAndFlush(actualAccount);

        entityManager.clear();

        assertThat(actualAccount).isEqualTo(accountRepository.findById(actualAccount.getId()).orElseThrow());

    }
}
