package com.example.businesslogic.deliveryorder.repository;

import com.example.businesslogic.deliveryorder.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
}
