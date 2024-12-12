package com.koanz.test.springboot.app.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class TransactionDto {
    @JsonProperty("account_origin_id")
    private Long accountOriginId;

    @JsonProperty("account_destiny_id")
    private Long accountDestinyId;

    @JsonProperty("bank_id")
    private Long bankId;
    private BigDecimal amount;

    public Long getAccountOriginId() {
        return accountOriginId;
    }

    public void setAccountOriginId(Long accountOriginId) {
        this.accountOriginId = accountOriginId;
    }

    public Long getAccountDestinyId() {
        return accountDestinyId;
    }

    public void setAccountDestinyId(Long accountDestinyId) {
        this.accountDestinyId = accountDestinyId;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
