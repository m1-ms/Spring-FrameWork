package com.example.businesslogic.reservation.repository;

import com.example.businesslogic.reservation.entity.RestaurantCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantCustomerRepository extends JpaRepository<RestaurantCustomer, Long> {
}
