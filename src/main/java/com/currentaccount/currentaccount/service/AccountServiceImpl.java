package com.currentaccount.currentaccount.service;

import com.currentaccount.currentaccount.exception.ValidationException;
import com.currentaccount.currentaccount.model.Account;
import com.currentaccount.currentaccount.model.InitialRequest;
import com.currentaccount.currentaccount.repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerService customerService;
    private final TransactionService transactionService;

    @Override
    public Account createAccountWithInitialCredit(InitialRequest initialRequest) {

        log.info("Creating account {}", initialRequest);
        if (initialRequest.initialCredit() == 0) {
            throw new ValidationException("Initial credit amount cannot be zero");
        }

        val customer = customerService.getCustomerById(initialRequest.customerId());

        val account = Account.builder()
                .customer(customer)
                .balance(initialRequest.initialCredit())
                .build();

        log.debug("Saving account {}", account);
        accountRepository.save(account);

        transactionService.createTransaction(account, initialRequest.initialCredit());

        customerService.updateCustomerBalance(customer, initialRequest.initialCredit());

        return account;
    }
}
