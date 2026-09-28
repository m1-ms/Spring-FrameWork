package com.example.businesslogic.productdiscount.repository;

import com.example.businesslogic.productdiscount.entity.DiscountProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountProductRepository extends JpaRepository<DiscountProduct, Long> {
}
