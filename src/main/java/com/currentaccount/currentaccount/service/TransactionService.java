package com.currentaccount.currentaccount.service;

import com.currentaccount.currentaccount.model.Account;
import com.currentaccount.currentaccount.model.Transaction;

public interface TransactionService {

    Transaction createTransaction(Account account, double amount);
}
