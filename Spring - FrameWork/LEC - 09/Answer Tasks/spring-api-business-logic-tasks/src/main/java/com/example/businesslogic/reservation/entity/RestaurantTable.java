package com.example.businesslogic.reservation.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "REST_TABLES")
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TABLE_NUMBER", nullable = false, unique = true)
    private int tableNumber;

    @Column(name = "CAPACITY", nullable = false)
    private int capacity;

    public RestaurantTable() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
