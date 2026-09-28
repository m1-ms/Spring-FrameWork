package com.example.businesslogic.productdiscount.entity;

import com.example.businesslogic.productdiscount.enums.CustomerType;
import jakarta.persistence.*;

@Entity
@Table(name = "DISC_CUSTOMERS")
public class DiscountCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CUSTOMER_NAME", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "CUSTOMER_TYPE", nullable = false)
    private CustomerType type;

    public DiscountCustomer() {
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

    public CustomerType getType() {
        return type;
    }

    public void setType(CustomerType type) {
        this.type = type;
    }
}
