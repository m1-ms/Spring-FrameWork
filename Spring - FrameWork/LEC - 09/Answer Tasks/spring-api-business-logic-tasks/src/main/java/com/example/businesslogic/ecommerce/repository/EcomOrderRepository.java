package com.example.businesslogic.ecommerce.repository;

import com.example.businesslogic.ecommerce.entity.EcomOrder;
import com.example.businesslogic.ecommerce.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EcomOrderRepository extends JpaRepository<EcomOrder, Long> {

    boolean existsByCustomerIdAndPaymentStatus(Long customerId, PaymentStatus paymentStatus);
}
