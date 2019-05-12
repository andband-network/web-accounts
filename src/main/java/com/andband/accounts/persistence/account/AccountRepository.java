package com.andband.accounts.persistence.account;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, String> {

    Account findByEmail(String email);

    void deleteAccountById(String id);

    @Modifying
    @Query("update Account set name = :name where accountId = :accountId")
    void updateNameWhereAccountId(String name, String accountId);

    @Modifying
    @Query("update Account set email = :email where accountId = :accountId")
    void updateEmailWhereAccountId(String email, String accountId);

}
