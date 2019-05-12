package com.andband.accounts.web;

import com.andband.accounts.config.web.resolver.UserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountsController {

    private AccountsService accountsService;

    public AccountsController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public AccountDTO getAccount(UserDetails userDetails) {
        return accountsService.getAccount(userDetails.getAccountId());
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

    @PostMapping("/{accountId}/name/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateName(@PathVariable("accountId") String accountId, @PathVariable("name") String name) {
        accountsService.updateName(name, accountId);
    }

    @PostMapping("/{accountId}/email/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAccountName(@PathVariable("accountId") String accountId, @PathVariable("email") String email) {
        accountsService.updateEmail(email, accountId);
    }

    @PostMapping("/{accountId}/password/{password}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(@PathVariable("accountId") String accountId, @PathVariable("password") String password) {
        accountsService.updatePassword(password, accountId);
    }

    @PostMapping("/forgot-password/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void forgotPassword(@PathVariable("email") String email) {
        accountsService.initiatePasswordReset(email);
    }

    @PostMapping("/reset-password/validate-token/{token}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isPasswordResetTokenValid(@PathVariable("token") String token) {
        return accountsService.isPasswordResetTokenValid(token);
    }

    @PostMapping("/reset-password/{token}/{password}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@PathVariable("token") String token, @PathVariable("password") String password) {
        accountsService.resetPassword(token, password);
    }

}
