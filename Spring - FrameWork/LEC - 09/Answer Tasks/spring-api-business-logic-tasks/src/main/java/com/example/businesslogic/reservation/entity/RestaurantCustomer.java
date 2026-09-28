package com.example.businesslogic.reservation.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "REST_CUSTOMERS")
public class RestaurantCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CUSTOMER_NAME", nullable = false)
    private String name;

    public RestaurantCustomer() {
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
}
