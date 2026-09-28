package com.example.businesslogic.employeeleave.enums;

public enum EmployeeType {

    FULL_TIME(21),
    PART_TIME(10),
    INTERN(5);

    private final int yearlyLeaveDays;

    EmployeeType(int yearlyLeaveDays) {
        this.yearlyLeaveDays = yearlyLeaveDays;
    }

    public int getYearlyLeaveDays() {
        return yearlyLeaveDays;
    }
}
