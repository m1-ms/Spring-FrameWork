package com.example.businesslogic.banktransfer.repository;

import com.example.businesslogic.banktransfer.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransferRepository extends JpaRepository<Transfer, Long> {

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transfer t " +
            "WHERE t.fromAccount.id = :accountId " +
            "AND t.createdAt >= :start AND t.createdAt < :end")
    BigDecimal sumTransferredBetween(@Param("accountId") Long accountId,
                                     @Param("start") LocalDateTime start,
                                     @Param("end") LocalDateTime end);
}
