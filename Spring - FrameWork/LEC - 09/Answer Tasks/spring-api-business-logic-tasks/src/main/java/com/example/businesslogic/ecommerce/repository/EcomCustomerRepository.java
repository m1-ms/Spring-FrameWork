package com.example.businesslogic.ecommerce.repository;

import com.example.businesslogic.ecommerce.entity.EcomCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EcomCustomerRepository extends JpaRepository<EcomCustomer, Long> {
}
