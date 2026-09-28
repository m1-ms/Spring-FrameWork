package com.example.businesslogic.parking.enums;

import java.math.BigDecimal;

public enum VehicleType {

    MOTORCYCLE(new BigDecimal("10"), new BigDecimal("5")),
    CAR(new BigDecimal("20"), new BigDecimal("10")),
    TRUCK(new BigDecimal("40"), new BigDecimal("20"));

    private final BigDecimal firstHourFee;
    private final BigDecimal additionalHourFee;

    VehicleType(BigDecimal firstHourFee, BigDecimal additionalHourFee) {
        this.firstHourFee = firstHourFee;
        this.additionalHourFee = additionalHourFee;
    }

    public BigDecimal getFirstHourFee() {
        return firstHourFee;
    }

    public BigDecimal getAdditionalHourFee() {
        return additionalHourFee;
    }
}
