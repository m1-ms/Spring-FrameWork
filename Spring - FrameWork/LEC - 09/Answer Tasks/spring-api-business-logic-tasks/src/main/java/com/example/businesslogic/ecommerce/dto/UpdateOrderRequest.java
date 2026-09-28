package com.example.businesslogic.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateOrderRequest {

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    public UpdateOrderRequest() {
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}