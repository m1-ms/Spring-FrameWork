package com.example.businesslogic.parking.repository;

import com.example.businesslogic.parking.entity.ParkingTicket;
import com.example.businesslogic.parking.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingTicketRepository extends JpaRepository<ParkingTicket, Long> {

    // Does the vehicle already have a ticket with this status
    boolean existsByVehicleIdAndStatus(Long vehicleId, TicketStatus status);

    // How many vehicles are currently inside the parking lot
    long countByParkingLotIdAndStatus(Long lotId, TicketStatus status);

    // The active ticket of a vehicle
    Optional<ParkingTicket> findByVehicleIdAndStatus(Long vehicleId, TicketStatus status);
}
