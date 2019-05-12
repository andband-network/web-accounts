package com.andband.accounts.persistence.account;

import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, String> {

    Account findByEmail(String email);

    void deleteAccountById(String id);

}
