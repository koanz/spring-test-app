package com.koanz.test.springboot.app.services.impl;

import com.koanz.test.springboot.app.models.Account;
import com.koanz.test.springboot.app.models.Bank;
import com.koanz.test.springboot.app.repositories.AccountRepository;
import com.koanz.test.springboot.app.repositories.BankRepository;
import com.koanz.test.springboot.app.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return repository.findById(id);
    }

    @Override
    public int getTotalTransfer(Long bancoId) {
        Bank bank = bankRepository.findById(bancoId);
        return bank.getTotalTransfer();
    }

    @Override
    public BigDecimal checkBalance(Long id) {
        Account account = repository.findById(id);

        /*if(account.getBalance().compareTo(BigDecimal.ZERO) < 0) {

        }*/

        return account.getBalance();
    }

    @Override
    public void transferMoneyFromTo(Long fromAccountId, Long toAccountId, BigDecimal amount, Long bankId) {
        Account accountOrigin = repository.findById(fromAccountId);
        Account accountDestiny = repository.findById(toAccountId);

        accountOrigin.debit(amount);
        repository.update(accountOrigin);

        accountDestiny.credit(amount);
        repository.update(accountDestiny);

        Bank bank = bankRepository.findById(bankId);
        int totalTransfer = bank.getTotalTransfer();
        bank.setTotalTransfer(++totalTransfer);
        bankRepository.update(bank);
    }

}
