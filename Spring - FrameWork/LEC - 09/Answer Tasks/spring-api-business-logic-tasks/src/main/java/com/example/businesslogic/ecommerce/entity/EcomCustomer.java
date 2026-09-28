package com.example.businesslogic.ecommerce.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ECOM_CUSTOMERS")
public class EcomCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CUSTOMER_NAME", nullable = false)
    private String name;

    public EcomCustomer() {
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
