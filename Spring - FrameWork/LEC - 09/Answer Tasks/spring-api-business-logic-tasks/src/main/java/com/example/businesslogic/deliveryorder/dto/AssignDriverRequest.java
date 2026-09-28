package com.example.businesslogic.deliveryorder.dto;

import jakarta.validation.constraints.NotNull;

public class AssignDriverRequest {

    @NotNull(message = "Driver id is required")
    private Long driverId;

    public AssignDriverRequest() {
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }
}