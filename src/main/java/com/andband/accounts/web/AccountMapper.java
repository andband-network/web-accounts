package com.andband.accounts.web;

import com.andband.accounts.persistence.account.Account;
import org.mapstruct.Mapper;

@Mapper
public interface AccountMapper {

    AccountDTO entityToDTO(Account account);

    Account dtoToEntity(AccountDTO accountDTO);

}
