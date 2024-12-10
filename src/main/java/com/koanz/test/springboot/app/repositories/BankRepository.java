package com.koanz.test.springboot.app.repositories;

import com.koanz.test.springboot.app.models.Bank;

import java.util.List;

public interface BankRepository {
    void save(Bank bank);
    Bank findById(Long id);
    void update(Bank bank);
    List<Bank> findAll();
}
