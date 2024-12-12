package com.koanz.test.springboot.app.services;

import com.koanz.test.springboot.app.models.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    Account save(String person, BigDecimal balance);
    //void update(Account account);
    Account findById(Long id);

    List<Account> findAll();

    int getTotalTransfer(Long bancoId);
    BigDecimal checkBalance(Long id);

    void wireMoneyFromTo(Long fromAccountId, Long toAccountId, BigDecimal amount, Long bankId);
}
