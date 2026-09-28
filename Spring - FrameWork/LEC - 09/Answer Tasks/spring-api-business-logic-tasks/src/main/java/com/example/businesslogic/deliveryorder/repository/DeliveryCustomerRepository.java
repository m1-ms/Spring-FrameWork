package com.example.businesslogic.deliveryorder.repository;

import com.example.businesslogic.deliveryorder.entity.DeliveryCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryCustomerRepository extends JpaRepository<DeliveryCustomer, Long> {
}
