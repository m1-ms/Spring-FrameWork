package com.example.businesslogic.parking.entity;

import com.example.businesslogic.parking.enums.VehicleType;
import jakarta.persistence.*;

@Entity
@Table(name = "PARK_VEHICLES")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Plate numbers are stored in upper case
    @Column(name = "PLATE_NUMBER", nullable = false, unique = true)
    private String plateNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "VEHICLE_TYPE", nullable = false)
    private VehicleType type;

    @Column(name = "IS_VIP", nullable = false)
    private boolean vip;

    public Vehicle() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }
}
