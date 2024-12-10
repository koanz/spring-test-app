package com.koanz.test.springboot.app;

import com.koanz.test.springboot.app.models.Account;
import com.koanz.test.springboot.app.models.Bank;

import java.math.BigDecimal;

public class Data {
    public static final Account ACCOUNT_001 = new Account(1L, "John Doe", new BigDecimal(10000));
    public static final Account ACCOUNT_002 = new Account(2L, "Jane Doe", new BigDecimal(10000));
    public static final Account ACCOUNT_003 = new Account(3L, "Roger Doe", new BigDecimal(10000));

    public static final Bank BANK = new Bank(1L, "Santander Rio", 0);

}
