package com.example.businesslogic.productdiscount.repository;

import com.example.businesslogic.productdiscount.entity.DiscountCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountCustomerRepository extends JpaRepository<DiscountCustomer, Long> {
}
