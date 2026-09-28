package com.example.businesslogic.library.enums;

public enum MembershipType {

    NORMAL(14),
    PREMIUM(30);

    private final int loanDays;

    MembershipType(int loanDays) {
        this.loanDays = loanDays;
    }

    public int getLoanDays() {
        return loanDays;
    }
}