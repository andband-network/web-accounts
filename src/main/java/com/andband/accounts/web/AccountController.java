package com.andband.accounts.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/accounts")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDTO createAccount(@RequestBody final AccountDTO account) {
        return accountService.createAccount(account);
    }

}
