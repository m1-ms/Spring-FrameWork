package com.example.businesslogic.productdiscount.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "DISC_CODES")
public class DiscountCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Codes are stored in upper case
    @Column(name = "CODE_VALUE", nullable = false, unique = true)
    private String code;

    @Column(name = "DISCOUNT_PERCENT", nullable = false, precision = 5, scale = 2)
    private BigDecimal discountPercent;

    // Two codes with the same non-null group cannot be combined
    @Column(name = "EXCLUSIVE_GROUP")
    private String exclusiveGroup;

    public DiscountCode() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getExclusiveGroup() {
        return exclusiveGroup;
    }

    public void setExclusiveGroup(String exclusiveGroup) {
        this.exclusiveGroup = exclusiveGroup;
    }
}
