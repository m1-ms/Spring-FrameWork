package com.example.businesslogic.productdiscount.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PriceRequest {

    @NotNull(message = "Customer id is required")
    private Long customerId;

    @NotEmpty(message = "At least one item is required")
    @Valid
    private List<PriceItemRequest> items;

    // Optional
    private List<String> discountCodes;

    public PriceRequest() {
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<PriceItemRequest> getItems() {
        return items;
    }

    public void setItems(List<PriceItemRequest> items) {
        this.items = items;
    }

    public List<String> getDiscountCodes() {
        return discountCodes;
    }

    public void setDiscountCodes(List<String> discountCodes) {
        this.discountCodes = discountCodes;
    }
}
