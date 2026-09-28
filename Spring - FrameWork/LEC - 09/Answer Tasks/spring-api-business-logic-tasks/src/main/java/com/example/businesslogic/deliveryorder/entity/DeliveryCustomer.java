package com.example.businesslogic.deliveryorder.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "DELIV_CUSTOMERS")
public class DeliveryCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CUSTOMER_NAME", nullable = false)
    private String name;

    public DeliveryCustomer() {
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
