package com.example.businesslogic.productdiscount.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "DISC_CATEGORIES")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CATEGORY_NAME", nullable = false)
    private String name;

    // Percentage, for example 10 means 10%
    @Column(name = "DISCOUNT_PERCENT", nullable = false, precision = 5, scale = 2)
    private BigDecimal discountPercent;

    public Category() {
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

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }
}
