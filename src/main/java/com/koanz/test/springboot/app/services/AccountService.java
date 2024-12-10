package com.koanz.test.springboot.app.services;

import com.koanz.test.springboot.app.models.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    //void save(Account account);
    //void update(Account account);
    Account findById(Long id);

    //List<Account> findAll();

    int getTotalTransfer(Long bancoId);
    BigDecimal checkBalance(Long id);

    void transferMoneyFromTo(Long fromAccountId, Long toAccountId, BigDecimal amount, Long bankId);
}
