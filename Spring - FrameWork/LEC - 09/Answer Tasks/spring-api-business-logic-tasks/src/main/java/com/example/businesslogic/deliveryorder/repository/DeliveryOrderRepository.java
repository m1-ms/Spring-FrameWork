package com.example.businesslogic.deliveryorder.repository;

import com.example.businesslogic.deliveryorder.entity.DeliveryOrder;
import com.example.businesslogic.deliveryorder.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface DeliveryOrderRepository extends JpaRepository<DeliveryOrder, Long> {

    // Does the driver have a delivery with any of these statuses
    boolean existsByDriverIdAndStatusIn(Long driverId, Collection<DeliveryStatus> statuses);
}
