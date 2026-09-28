package com.example.businesslogic.ecommerce.repository;

import com.example.businesslogic.ecommerce.entity.EcomProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EcomProductRepository extends JpaRepository<EcomProduct, Long> {
}
