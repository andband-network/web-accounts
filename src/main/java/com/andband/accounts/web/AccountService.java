package com.andband.accounts.web;

import com.andband.accounts.exception.ApplicationException;
import com.andband.accounts.persistence.Account;
import com.andband.accounts.persistence.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private AccountRepository accountRepository;
    private AccountMapper accountMapper;

    AccountService(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    AccountDTO createAccount(AccountDTO accountDTO) {
        Account account;

        synchronized (this) {
            if (accountExists(accountDTO.getEmail())) {
                throw new ApplicationException("an account exists with email: " + accountDTO.getEmail());
            }
            account = accountMapper.dtoToEntity(accountDTO);
            account.setEnabled(true);
            accountRepository.save(account);
        }

        return accountMapper.entityToDTO(account);
    }

    private boolean accountExists(String email) {
        Account account = accountRepository.findByEmail(email);
        return account != null;
    }

}
