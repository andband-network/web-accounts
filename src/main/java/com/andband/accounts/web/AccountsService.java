package com.andband.accounts.web;

import com.andband.accounts.client.auth.AuthService;
import com.andband.accounts.client.notification.NotificationService;
import com.andband.accounts.exception.ApplicationException;
import com.andband.accounts.persistence.account.Account;
import com.andband.accounts.persistence.account.AccountRepository;
import com.andband.accounts.persistence.token.PasswordResetToken;
import com.andband.accounts.service.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountsService {

    private AccountRepository accountRepository;
    private AccountMapper accountMapper;
    private TokenService tokenService;
    private NotificationService notificationService;
    private AuthService authService;

    public AccountsService(AccountRepository accountRepository,
                           AccountMapper accountMapper,
                           TokenService tokenService,
                           NotificationService notificationService,
                           AuthService authService) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.tokenService = tokenService;
        this.notificationService = notificationService;
        this.authService = authService;
    }

    AccountDTO getAccount(String accountId) {
        Account account = findAccountById(accountId);
        return accountMapper.entityToDTO(account);
    }

    AccountDTO createAccount(AccountDTO accountDTO) {
        Account account;

        synchronized (this) {
            if (accountExists(accountDTO.getEmail())) {
                throw new ApplicationException("an account exists with email: " + accountDTO.getEmail());
            }
            account = accountMapper.dtoToEntity(accountDTO);
            accountRepository.save(account);
        }

        return accountMapper.entityToDTO(account);
    }

    void deleteAccount(String accountId) {
        accountRepository.deleteAccountById(accountId);
    }

    @Transactional
    public void updateName(String name, String accountId) {
        accountRepository.updateNameWhereAccountId(name, accountId);
    }

    @Transactional
    public void updateEmail(String email, String accountId) {
        accountRepository.updateEmailWhereAccountId(email, accountId);
    }

    void updatePassword(String password, String accountId) {
        authService.updatedPassword(password, accountId);
    }

    void initiatePasswordReset(String email) {
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            throw new ApplicationException("not account exists with email " + email);
        }
        String token = tokenService.generatePasswordResetToken(account.getId());
        notificationService.resetPassword(email, account.getName(), token);
    }

    boolean isPasswordResetTokenValid(String tokenString) {
        return tokenService.isPasswordResetTokenValid(tokenString);
    }

    void resetPassword(String tokenString, String password) {
        PasswordResetToken token = tokenService.getPasswordResetToken(tokenString);
        if (!tokenService.isTokenValid(token)) {
            throw new ApplicationException("token is either invalid or expired");
        }
        authService.updatedPassword(password, token.getAccountId());
    }

    private Account findAccountById(String accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        if (!account.isPresent()) {
            throw new ApplicationException("could not find account with id " + accountId);
        }
        return account.get();
    }

    private boolean accountExists(String email) {
        Account account = accountRepository.findByEmail(email);
        return account != null;
    }

}
