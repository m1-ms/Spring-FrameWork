package com.example.businesslogic.deliveryorder.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class CreateDeliveryRequest {

    @NotNull(message = "Customer id is required")
    private Long customerId;

    @NotNull(message = "Distance is required")
    @Positive(message = "Distance must be greater than zero")
    private BigDecimal distanceKm;

    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be greater than zero")
    private BigDecimal weightKg;

    public CreateDeliveryRequest() {
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(BigDecimal distanceKm) {
        this.distanceKm = distanceKm;
    }

    public BigDecimal getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(BigDecimal weightKg) {
        this.weightKg = weightKg;
    }
}
