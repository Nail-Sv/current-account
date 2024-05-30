package com.currentaccount.currentaccount.service;

import com.currentaccount.currentaccount.exception.ValidationException;
import com.currentaccount.currentaccount.model.Account;
import com.currentaccount.currentaccount.model.Customer;
import com.currentaccount.currentaccount.model.InitialRequest;
import com.currentaccount.currentaccount.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private AccountServiceImpl accountService;

    private InitialRequest initialRequest;
    private Customer customer;
    private Account account;

    @BeforeEach
    void setUp() {
        initialRequest = new InitialRequest(UUID.randomUUID(), 1000.0);
        customer = new Customer();
        customer.setId(initialRequest.customerId());

        account = Account.builder()
                .customer(customer)
                .balance(initialRequest.initialCredit())
                .build();
    }

    @Test
    void testCreateAccountWithInitialCredit_ShouldCreateAccount() {

        when(customerService.getCustomerById(initialRequest.customerId())).thenReturn(customer);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account createdAccount = accountService.createAccountWithInitialCredit(initialRequest);

        verify(customerService).getCustomerById(initialRequest.customerId());
        verify(accountRepository).save(any(Account.class));
        verify(transactionService).createTransaction(any(Account.class), eq(initialRequest.initialCredit()));
        verify(customerService).updateCustomerBalance(customer, initialRequest.initialCredit());

        assertEquals(initialRequest.initialCredit(), createdAccount.getBalance());
        assertEquals(customer, createdAccount.getCustomer());
    }

    @Test
    void testCreateAccountWithZeroInitialCredit_ShouldThrowsException() {

        initialRequest = new InitialRequest(UUID.randomUUID(), 0.0);

        assertThrows(ValidationException.class, () -> {
            accountService.createAccountWithInitialCredit(initialRequest);
        });

        verifyNoInteractions(customerService);
        verifyNoInteractions(accountRepository);
        verifyNoInteractions(transactionService);
    }
}
