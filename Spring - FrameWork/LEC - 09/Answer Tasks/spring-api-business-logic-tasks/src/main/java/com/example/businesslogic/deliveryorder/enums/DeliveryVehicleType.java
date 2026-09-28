package com.example.businesslogic.deliveryorder.enums;

import java.math.BigDecimal;

public enum DeliveryVehicleType {

    BIKE(new BigDecimal("5"), false),
    MOTORCYCLE(new BigDecimal("20"), false),
    CAR(new BigDecimal("100"), true),
    VAN(new BigDecimal("500"), true);

    private final BigDecimal maxWeightKg;
    private final boolean longDistanceCapable;

    DeliveryVehicleType(BigDecimal maxWeightKg, boolean longDistanceCapable) {
        this.maxWeightKg = maxWeightKg;
        this.longDistanceCapable = longDistanceCapable;
    }

    public BigDecimal getMaxWeightKg() {
        return maxWeightKg;
    }

    public boolean isLongDistanceCapable() {
        return longDistanceCapable;
    }
}
