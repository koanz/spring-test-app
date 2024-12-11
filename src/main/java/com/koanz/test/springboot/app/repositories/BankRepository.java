package com.koanz.test.springboot.app.repositories;

import com.koanz.test.springboot.app.models.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank, Long> {
    /*void save(Bank bank);
    Bank findById(Long id);
    void update(Bank bank);
    List<Bank> findAll();*/
}
