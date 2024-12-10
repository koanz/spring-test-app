package com.koanz.test.springboot.app.repositories;

import com.koanz.test.springboot.app.models.Account;

import java.util.List;

public interface AccountRepository {
    void save(Account account);
    void update(Account account);
    Account findById(Long id);
    List<Account> findAll();
}
