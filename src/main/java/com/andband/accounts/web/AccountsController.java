package com.andband.accounts.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountsController {

    private AccountsService accountsService;

    public AccountsController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @GetMapping("/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public AccountDTO getAccount(@PathVariable("accountId") String accountId) {
        return accountsService.getAccount(accountId);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDTO createAccount(@RequestBody AccountDTO account) {
        return accountsService.createAccount(account);
    }

    @DeleteMapping("/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable("accountId") String accountId) {
        accountsService.deleteAccount(accountId);
    }

}
