package com.example.businesslogic.parking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "PARK_LOTS")
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "LOT_NAME", nullable = false)
    private String name;

    @Column(name = "CAPACITY", nullable = false)
    private int capacity;

    public ParkingLot() {
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}