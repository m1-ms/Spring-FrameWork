package com.example.businesslogic.reservation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class ReservationRequest {

    @NotNull(message = "Table id is required")
    private Long tableId;

    @NotNull(message = "Customer id is required")
    private Long customerId;

    @NotNull(message = "Number of guests is required")
    @Positive(message = "Number of guests must be greater than zero")
    private Integer guests;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    public ReservationRequest() {
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Integer getGuests() {
        return guests;
    }

    public void setGuests(Integer guests) {
        this.guests = guests;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
