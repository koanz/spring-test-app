package com.koanz.test.springboot.app;

import com.koanz.test.springboot.app.models.Account;
import com.koanz.test.springboot.app.models.Bank;

import java.math.BigDecimal;
import java.util.Optional;

public class Data {

    public static Optional<Account> createAccount001() {
        return Optional.of(new Account(1L, "John Doe", new BigDecimal("1000")));
    }

    public static Optional<Account> createAccount002() {
        return Optional.of(new Account(2L, "Jane Doe", new BigDecimal("2000")));
    }

    public static Optional<Bank> createBank() {
        return Optional.of(new Bank(1L, "Santander Rio", 0));
    }
}
