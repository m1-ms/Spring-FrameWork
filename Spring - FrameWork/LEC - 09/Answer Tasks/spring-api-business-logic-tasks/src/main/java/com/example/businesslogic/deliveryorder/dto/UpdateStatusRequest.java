package com.example.businesslogic.deliveryorder.dto;

import com.example.businesslogic.deliveryorder.enums.DeliveryStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateStatusRequest {

    @NotNull(message = "Status is required")
    private DeliveryStatus status;

    public UpdateStatusRequest() {
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }
}
