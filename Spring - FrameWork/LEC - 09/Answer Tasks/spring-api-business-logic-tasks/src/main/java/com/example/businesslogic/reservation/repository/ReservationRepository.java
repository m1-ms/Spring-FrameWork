package com.example.businesslogic.reservation.repository;

import com.example.businesslogic.reservation.entity.Reservation;
import com.example.businesslogic.reservation.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT COUNT(r) FROM Reservation r " +
            "WHERE r.restaurantTable.id = :tableId AND r.status IN :statuses " +
            "AND r.startTime < :newEnd AND r.endTime > :newStart")
    long countOverlapping(@Param("tableId") Long tableId,
                          @Param("statuses") Collection<ReservationStatus> statuses,
                          @Param("newStart") LocalDateTime newStart,
                          @Param("newEnd") LocalDateTime newEnd);

    // Active reservations of a customer that did not finish yet
    @Query("SELECT COUNT(r) FROM Reservation r " +
            "WHERE r.customer.id = :customerId AND r.status IN :statuses " +
            "AND r.endTime > :now")
    long countActiveByCustomer(@Param("customerId") Long customerId,
                               @Param("statuses") Collection<ReservationStatus> statuses,
                               @Param("now") LocalDateTime now);

    // Pending reservations created before the threshold become expired
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Reservation r SET r.status = :expired " +
            "WHERE r.status = :pending AND r.createdAt < :threshold")
    int expireStale(@Param("expired") ReservationStatus expired,
                    @Param("pending") ReservationStatus pending,
                    @Param("threshold") LocalDateTime threshold);
}