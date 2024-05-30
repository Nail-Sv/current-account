package com.currentaccount.currentaccount.controller;

import com.currentaccount.currentaccount.dto.AccountDTO;
import com.currentaccount.currentaccount.dto.InitialRequestDTO;
import com.currentaccount.currentaccount.mapper.InitialRequestMapper;
import com.currentaccount.currentaccount.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AccountControllerImpl implements AccountController {

    private final AccountService accountService;
    private final InitialRequestMapper initialRequestMapper;

    @Override
    public ResponseEntity<AccountDTO> createAccount(InitialRequestDTO initialRequestDTO) {

        val createdAccount = accountService.createAccountWithInitialCredit(
                initialRequestMapper.toEntity(initialRequestDTO)
        );

        val headers = new HttpHeaders();

        headers.add("Location",
                    AccountController.REST_URL + "/" + createdAccount.getId().toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
