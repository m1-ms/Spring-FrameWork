package com.example.businesslogic.banktransfer.enums;

import java.math.BigDecimal;

public enum AccountType {

    SAVINGS(new BigDecimal("10000")),
    CURRENT(new BigDecimal("50000")),
    BUSINESS(new BigDecimal("200000"));

    private final BigDecimal dailyLimit;

    AccountType(BigDecimal dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public BigDecimal getDailyLimit() {
        return dailyLimit;
    }
}
