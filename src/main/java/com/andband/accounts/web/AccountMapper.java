package com.andband.accounts.web;

import com.andband.accounts.persistence.Account;
import org.mapstruct.Mapper;

@Mapper
public interface AccountMapper {

    AccountDTO entityToDTO(Account account);

    Account dtoToEntity(AccountDTO accountDTO);

}
