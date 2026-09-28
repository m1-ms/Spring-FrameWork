package com.example.businesslogic.parking.dto;

import com.example.businesslogic.parking.enums.TicketStatus;
import com.example.businesslogic.parking.enums.VehicleType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TicketResponse {

    private Long ticketId;
    private String plateNumber;
    private VehicleType vehicleType;
    private boolean vip;
    private Long lotId;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Integer billedHours;
    private BigDecimal fee;
    private TicketStatus status;
    private String message;

    public TicketResponse() {
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public Long getLotId() {
        return lotId;
    }

    public void setLotId(Long lotId) {
        this.lotId = lotId;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public Integer getBilledHours() {
        return billedHours;
    }

    public void setBilledHours(Integer billedHours) {
        this.billedHours = billedHours;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
