package com.example.businesslogic.reservation.repository;

import com.example.businesslogic.reservation.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
}
