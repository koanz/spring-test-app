package com.koanz.test.springboot.app;

import com.koanz.test.springboot.app.models.Account;
import com.koanz.test.springboot.app.models.Bank;

import java.math.BigDecimal;

public class Data {

    public static Account createAccount001() {
        return new Account(1L, "John Doe", new BigDecimal("10000"));
    }

    public static Account createAccount002() {
        return new Account(2L, "Jane Doe", new BigDecimal("3000"));
    }

    public static Bank createBank() {
        return new Bank(1L, "Santander Rio", 0);
    }
}
