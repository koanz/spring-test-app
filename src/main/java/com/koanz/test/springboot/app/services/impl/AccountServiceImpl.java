package com.koanz.test.springboot.app.services.impl;

import com.koanz.test.springboot.app.models.Account;
import com.koanz.test.springboot.app.models.Bank;
import com.koanz.test.springboot.app.repositories.AccountRepository;
import com.koanz.test.springboot.app.repositories.BankRepository;
import com.koanz.test.springboot.app.services.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    private AccountRepository repository;
    private BankRepository bankRepository;

    public AccountServiceImpl(AccountRepository repository, BankRepository bankRepository) {
        this.repository = repository;
        this.bankRepository = bankRepository;
    }

    @Override
    @Transactional
    public Account save(Account account) {
        Optional<Account> accountOpt = repository.findByPerson(account.getPerson());

        if(accountOpt.isPresent()) {
            throw new RuntimeException("Account of this persona already exists: " + account.getPerson());
        }

        Account newAccount = new Account();
        newAccount.setPerson(account.getPerson());
        newAccount.setBalance(account.getBalance());

        newAccount = repository.save(newAccount);

        return newAccount;
    }

    @Override
    @Transactional(readOnly = true)
    public Account findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public int getTotalTransfer(Long bancoId) {
        Bank bank = bankRepository.findById(bancoId).orElseThrow();
        return bank.getTotalTransfer();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal checkBalance(Long id) {
        Account account = repository.findById(id).orElseThrow();

        return account.getBalance();
    }

    @Override
    @Transactional
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

    @Override
    @Transactional
    public void delete(Long id) {
        Account account = repository.findById(id).orElseThrow();
        repository.delete(account);
    }

}
