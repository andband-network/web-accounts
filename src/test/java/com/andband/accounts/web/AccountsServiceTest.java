package com.andband.accounts.web;

import com.andband.accounts.exception.ApplicationException;
import com.andband.accounts.persistence.Account;
import com.andband.accounts.persistence.AccountRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AccountsServiceTest {

    @InjectMocks
    private AccountsService accountsService;

    @Mock
    private AccountRepository mockAccountRepository;

    @Mock
    private AccountMapper mockAccountMapper;

    @BeforeMethod
    public void init() {
        initMocks(this);
    }

    @Test
    public void testGetAccount() {
        String accountId = "account123";

        AccountDTO expectedAccount = new AccountDTO();
        Optional<Account> accountEntity = Optional.of(new Account());

        when(mockAccountRepository.findById(accountId)).thenReturn(accountEntity);
        when(mockAccountMapper.entityToDTO(accountEntity.get())).thenReturn(expectedAccount);

        AccountDTO actualAccount = accountsService.getAccount(accountId);

        assertThat(actualAccount).isEqualTo(expectedAccount);

        verify(mockAccountRepository).findById(accountId);
        verify(mockAccountMapper).entityToDTO(accountEntity.get());
    }

    @Test(expectedExceptions = ApplicationException.class)
    public void testGetAccountDoesNotExist() {
        String accountId = "account123";

        Optional<Account> accountEntity = Optional.empty();

        when(mockAccountRepository.findById(accountId)).thenReturn(accountEntity);

        accountsService.getAccount(accountId);
    }

    @Test
    public void testCreateAccount() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setEmail("user1@email.com");

        Account accountEntity = new Account();

        AccountDTO expectedAccount = new AccountDTO();

        when(mockAccountRepository.findByEmail(accountDTO.getEmail())).thenReturn(null);
        when(mockAccountMapper.dtoToEntity(accountDTO)).thenReturn(accountEntity);
        when(mockAccountMapper.entityToDTO(accountEntity)).thenReturn(expectedAccount);

        AccountDTO actualAccount = accountsService.createAccount(accountDTO);

        assertThat(actualAccount).isEqualTo(expectedAccount);

        verify(mockAccountRepository).findByEmail(accountDTO.getEmail());
        verify(mockAccountMapper).dtoToEntity(accountDTO);
        verify(mockAccountRepository).save(accountEntity);
        verify(mockAccountMapper).entityToDTO(accountEntity);
    }

    @Test(expectedExceptions = ApplicationException.class)
    public void testCreateAccountAlreadyExists() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setEmail("user1@email.com");

        when(mockAccountRepository.findByEmail(accountDTO.getEmail())).thenReturn(new Account());

        accountsService.createAccount(accountDTO);
    }

    @Test
    public void testDeleteAccount() {
        String accountId = "account123";

        accountsService.deleteAccount(accountId);

        verify(mockAccountRepository).deleteAccountById(accountId);
    }

}
