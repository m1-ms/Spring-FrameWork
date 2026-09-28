package com.example.businesslogic.productdiscount.dto;

import com.example.businesslogic.productdiscount.enums.CustomerType;

import java.math.BigDecimal;
import java.util.List;

public class PriceResponse {

    private Long customerId;
    private CustomerType customerType;
    private List<String> appliedCodes;
    private List<PriceItemResponse> items;
    private BigDecimal subtotal;
    private BigDecimal totalDiscount;
    private BigDecimal amountAfterDiscount;
    private BigDecimal shippingFee;
    private boolean freeShipping;
    private BigDecimal finalTotal;

    public PriceResponse() {
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public List<String> getAppliedCodes() {
        return appliedCodes;
    }

    public void setAppliedCodes(List<String> appliedCodes) {
        this.appliedCodes = appliedCodes;
    }

    public List<PriceItemResponse> getItems() {
        return items;
    }

    public void setItems(List<PriceItemResponse> items) {
        this.items = items;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public BigDecimal getAmountAfterDiscount() {
        return amountAfterDiscount;
    }

    public void setAmountAfterDiscount(BigDecimal amountAfterDiscount) {
        this.amountAfterDiscount = amountAfterDiscount;
    }

    public BigDecimal getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(BigDecimal shippingFee) {
        this.shippingFee = shippingFee;
    }

    public boolean isFreeShipping() {
        return freeShipping;
    }

    public void setFreeShipping(boolean freeShipping) {
        this.freeShipping = freeShipping;
    }

    public BigDecimal getFinalTotal() {
        return finalTotal;
    }

    public void setFinalTotal(BigDecimal finalTotal) {
        this.finalTotal = finalTotal;
    }
}
