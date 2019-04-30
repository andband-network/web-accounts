package com.andband.accounts.web;

import com.andband.accounts.exception.ApplicationException;
import com.andband.accounts.persistence.Account;
import com.andband.accounts.persistence.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountsService {

    private AccountRepository accountRepository;
    private AccountMapper accountMapper;

    AccountsService(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    AccountDTO getAccount(String accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        if (!account.isPresent()) {
            throw new ApplicationException("could not find account with id " + accountId);
        }
        return accountMapper.entityToDTO(account.get());
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

    private boolean accountExists(String email) {
        Account account = accountRepository.findByEmail(email);
        return account != null;
    }

}
