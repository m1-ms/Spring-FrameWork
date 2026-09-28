package com.example.businesslogic.deliveryorder.entity;

import com.example.businesslogic.deliveryorder.enums.DeliveryVehicleType;
import jakarta.persistence.*;

@Entity
@Table(name = "DELIV_DRIVERS")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DRIVER_NAME", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "VEHICLE_TYPE", nullable = false)
    private DeliveryVehicleType vehicleType;

    public Driver() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeliveryVehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(DeliveryVehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
}
