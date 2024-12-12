package com.koanz.test.springboot.app.models.dtos;

import java.math.BigDecimal;

public class AccountDto {
    private String person;
    private BigDecimal balance;

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
