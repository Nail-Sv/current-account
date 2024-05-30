package com.currentaccount.currentaccount.service;

import com.currentaccount.currentaccount.model.Account;
import com.currentaccount.currentaccount.model.InitialRequest;

public interface AccountService {

    Account createAccountWithInitialCredit(InitialRequest initialRequest);

}
