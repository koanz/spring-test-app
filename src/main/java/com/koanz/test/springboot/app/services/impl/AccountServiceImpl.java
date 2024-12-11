package com.koanz.test.springboot.app.services.impl;

import com.koanz.test.springboot.app.models.Account;
import com.koanz.test.springboot.app.models.Bank;
import com.koanz.test.springboot.app.repositories.AccountRepository;
import com.koanz.test.springboot.app.repositories.BankRepository;
import com.koanz.test.springboot.app.services.AccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {
    private AccountRepository repository;
    private BankRepository bankRepository;

    public AccountServiceImpl(AccountRepository repository, BankRepository bankRepository) {
        this.repository = repository;
        this.bankRepository = bankRepository;
    }

    @Override
    public Account findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public int getTotalTransfer(Long bancoId) {
        Bank bank = bankRepository.findById(bancoId).orElseThrow();
        return bank.getTotalTransfer();
    }

    @Override
    public BigDecimal checkBalance(Long id) {
        Account account = repository.findById(id).orElseThrow();

        return account.getBalance();
    }

    @Override
    public void wireMoneyFromTo(Long fromAccountId, Long toAccountId, BigDecimal amount, Long bankId) {
        Account accountOrigin = repository.findById(fromAccountId).orElseThrow();
        accountOrigin.debit(amount);
        repository.save(accountOrigin);

        Account accountDestiny = repository.findById(toAccountId).orElseThrow();
        accountDestiny.credit(amount);
        repository.save(accountDestiny);

        Bank bank = bankRepository.findById(bankId).orElseThrow();

        int totalTransfer = bank.getTotalTransfer();
        bank.setTotalTransfer(++totalTransfer);
        bankRepository.save(bank);
    }

}
